package cn.iocoder.yudao.module.member.service.signin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.module.member.controller.admin.signin.vo.record.MemberSignInRecordPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.signin.vo.record.AppMemberSignInRecordSummaryRespVO;
import cn.iocoder.yudao.module.member.convert.signin.MemberSignInRecordConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import cn.iocoder.yudao.module.member.dal.dataobject.signin.MemberSignInRecordDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.signin.MemberSignInRecordMapper;
import cn.iocoder.yudao.module.member.enums.MemberExperienceBizTypeEnum;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.point.MemberPointRecordService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.SIGN_IN_RECORD_TODAY_EXISTS;

/**
 * 签到记录 Service 实现类
 *
 * @author 商城管理系统
 */
@Service
@Validated
public class MemberSignInRecordServiceImpl implements MemberSignInRecordService {

    @Resource
    private MemberSignInRecordMapper signInRecordMapper;

    @Resource
    private MemberSignInConfigService signInConfigService;

    @Resource
    private MemberPointRecordService pointRecordService;

    @Resource
    private MemberLevelService memberLevelService;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public AppMemberSignInRecordSummaryRespVO getSignInRecordSummary(Long userId) {
        // 1. 初始化默认返回信息
        AppMemberSignInRecordSummaryRespVO summary = new AppMemberSignInRecordSummaryRespVO();
        summary.setTotalDay(0);
        summary.setContinuousDay(0);
        summary.setTodaySignIn(false);

        // 2. 获取用户签到的记录数
        Long signCount = signInRecordMapper.selectCountByUserId(userId);
        if (ObjUtil.equal(signCount, 0L)) {
            return summary;
        }
        summary.setTotalDay(signCount.intValue()); // 设置总签到天数

        // 3. 校验当天是否有签到
        MemberSignInRecordDO lastRecord = signInRecordMapper.selectLastRecordByUserId(userId);
        if (lastRecord == null) {
            return summary;
        }
        summary.setTodaySignIn(DateUtils.isToday(lastRecord.getCreateTime()));
        summary.setLastTime(lastRecord.getCreateTime());

        //判断最后一次签到是不是昨天或者今天，不是的化，连续签到天数归0
        LocalDateTime yesterdayDate = LocalDateTime.now().plusDays(-1);
        if (!LocalDateTimeUtil.isSameDay(summary.getLastTime(),yesterdayDate) && !summary.getTodaySignIn()){
            return summary;
        }

//        // 4. 校验今天是否签到，没有签到则直接返回
//        if (!summary.getTodaySignIn()) {
//            return summary;
//        }
        // 4.1. 判断连续签到天数
        // TODO @puhui999：连续签到，可以基于 lastRecord 的 day 和当前时间判断呀？按 day 统计连续签到天数可能不准确
        //      1. day 只是记录第几天签到的有可能不连续，比如第一次签到是周一，第二次签到是周三这样 lastRecord 的 day 为 2 但是并不是连续的两天
        //      2. day 超出签到规则的最大天数会重置到从第一天开始签到（我理解为开始下一轮，类似一周签到七天七天结束下周又从周一开始签到）
        // 1. 回复：周三签到，day 要归 1 呀。连续签到哈；
        List<MemberSignInRecordDO> signInRecords = signInRecordMapper.selectListByUserId(userId);
        signInRecords.sort(Comparator.comparing(MemberSignInRecordDO::getCreateTime).reversed()); // 根据签到时间倒序
        Integer continuousDay = calculateConsecutiveDays(signInRecords);
        //如果连续签到天数超过配置天数，重新开始计算
        if (continuousDay.compareTo(0)>0){
            List<MemberSignInConfigDO> result = signInConfigService.getSignInConfigList(CommonStatusEnum.ENABLE.getStatus());
            if (result!=null && result.size()>0){
                continuousDay = continuousDay % result.size();

                if (continuousDay.compareTo(0)==0){
                    continuousDay = result.size();
                }
            }
        }
        summary.setContinuousDay(continuousDay);
        return summary;
    }

    @Override
    public Boolean isSign(Long userId, Integer day) {
        Boolean result = false;
        if (userId!=null && day!=null){
            LocalDateTime queryDate = LocalDateTime.now();
            if (day>1){
                day = day-1;
                queryDate = queryDate.plusDays(-day);
            }
            LocalDateTime startTime = toFormatLocalDateTime(queryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 00:00:00");
            LocalDateTime endTime =toFormatLocalDateTime(queryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 23:59:59");
            LambdaQueryWrapper<MemberSignInRecordDO> wrapper = new LambdaQueryWrapper<>();
            wrapper = wrapper.eq(MemberSignInRecordDO::getUserId,userId).between(MemberSignInRecordDO::getCreateTime,startTime,endTime);
            Long res = signInRecordMapper.selectCount(wrapper);
            if (res!=null && res.compareTo(0L)>0){
                result = true;
            }
        }
        return result;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public LocalDateTime toFormatLocalDateTime(String dateTime){
        LocalDateTime result = null;
        if (StringUtils.isNotEmpty(dateTime)){
            String formatter = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
            result = LocalDateTime.parse(dateTime,df);
        }
        return result;
    }

    /**
     * 计算连续签到天数
     *
     * @param signInRecords 签到记录列表
     * @return int 连续签到天数
     */
    public int calculateConsecutiveDays(List<MemberSignInRecordDO> signInRecords) {
        int consecutiveDays = 1;  // 初始连续天数为1
        LocalDate previousDate = null;

        for (MemberSignInRecordDO record : signInRecords) {
            LocalDate currentDate = record.getCreateTime().toLocalDate();

            if (previousDate != null) {
                // 检查相邻两个日期是否连续
                if (previousDate.minusDays(1).isEqual(currentDate)) {
                    consecutiveDays++;
                } else {
                    // 如果日期不连续，停止遍历
                    break;
                }
            }

            previousDate = currentDate;
        }

        return consecutiveDays;
    }

    @Override
    public PageResult<MemberSignInRecordDO> getSignInRecordPage(MemberSignInRecordPageReqVO pageReqVO) {
        // 根据用户昵称查询出用户ids
        Set<Long> userIds = null;
        if (StringUtils.isNotBlank(pageReqVO.getNickname())) {
            List<MemberUserDO> users = memberUserService.getUserListByNickname(pageReqVO.getNickname());
            // 如果查询用户结果为空直接返回无需继续查询
            if (CollUtil.isEmpty(users)) {
                return PageResult.empty();
            }
            userIds = convertSet(users, MemberUserDO::getId);
        }
        // 分页查询
        return signInRecordMapper.selectPage(pageReqVO, userIds);
    }

    @Override
    public PageResult<MemberSignInRecordDO> getSignRecordPage(Long userId, PageParam pageParam) {
        return signInRecordMapper.selectPage(userId, pageParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberSignInRecordDO createSignRecord(Long userId) {
        // 1. 获取当前用户最近的签到
        MemberSignInRecordDO lastRecord = signInRecordMapper.selectLastRecordByUserId(userId);
        // 1.1. 判断是否重复签到
        validateSigned(lastRecord);

        // 2.1. 获取所有的签到规则
        List<MemberSignInConfigDO> signInConfigs = signInConfigService.getSignInConfigList(CommonStatusEnum.ENABLE.getStatus());
        // 2.2. 组合数据
        MemberSignInRecordDO record = MemberSignInRecordConvert.INSTANCE.convert(userId, lastRecord, signInConfigs);

        // 3. 插入签到记录
        signInRecordMapper.insert(record);

        // 4. 增加积分
        if (!ObjectUtils.equalsAny(record.getPoint(), null, 0)) {
            pointRecordService.createPointRecord(userId, record.getPoint(), MemberPointBizTypeEnum.SIGN, String.valueOf(record.getId()));
        }
        // 5. 增加经验
        if (!ObjectUtils.equalsAny(record.getExperience(), null, 0)) {
            memberLevelService.addExperience(userId, record.getExperience(), MemberExperienceBizTypeEnum.SIGN_IN, String.valueOf(record.getId()));
        }
        return record;
    }

    private void validateSigned(MemberSignInRecordDO signInRecordDO) {
        if (signInRecordDO == null) {
            return;
        }
        if (DateUtils.isToday(signInRecordDO.getCreateTime())) {
            throw exception(SIGN_IN_RECORD_TODAY_EXISTS);
        }
    }

}
