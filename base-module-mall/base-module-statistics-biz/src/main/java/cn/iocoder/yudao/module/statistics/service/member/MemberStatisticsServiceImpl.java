package cn.iocoder.yudao.module.statistics.service.member;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.enums.AreaTypeEnum;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.DataComparisonRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.*;
import cn.iocoder.yudao.module.statistics.convert.member.MemberStatisticsConvert;
import cn.iocoder.yudao.module.statistics.dal.mysql.member.MemberStatisticsMapper;
import cn.iocoder.yudao.module.statistics.service.infra.ApiAccessLogStatisticsService;
import cn.iocoder.yudao.module.statistics.service.member.bo.MemberAreaStatisticsRespBO;
import cn.iocoder.yudao.module.statistics.service.pay.PayWalletStatisticsService;
import cn.iocoder.yudao.module.statistics.service.pay.bo.RechargeSummaryRespBO;
import cn.iocoder.yudao.module.statistics.service.trade.TradeOrderStatisticsService;
import cn.iocoder.yudao.module.statistics.service.trade.TradeStatisticsService;
import cn.iocoder.yudao.module.system.enums.common.SexEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 会员信息的统计 Service 实现类
 *
 * @author owen
 */
@Service
@Validated
public class MemberStatisticsServiceImpl implements MemberStatisticsService {

    @Resource
    private MemberStatisticsMapper memberStatisticsMapper;

    @Resource
    private PayWalletStatisticsService payWalletStatisticsService;
    @Resource
    private TradeStatisticsService tradeStatisticsService;
    @Resource
    private TradeOrderStatisticsService tradeOrderStatisticsService;
    @Resource
    private ApiAccessLogStatisticsService apiAccessLogStatisticsService;

    @Override
    public MemberSummaryRespVO getMemberSummary() {
        RechargeSummaryRespBO rechargeSummary = payWalletStatisticsService.getUserRechargeSummary(null, null);
        // TODO @疯狂：1）这里是实时统计，不好走走 TradeStatistics 表；2）因为这个放在商城下，所以只考虑订单数据，即按照 trade_order 的 pay_price 并且已支付来计算；
        Integer expensePrice = tradeStatisticsService.getExpensePrice(null, null);
        Integer userCount = memberStatisticsMapper.selectUserCount(null, null);
        return MemberStatisticsConvert.INSTANCE.convert(rechargeSummary, expensePrice, userCount);
    }

    @Override
    public List<MemberAreaStatisticsRespVO> getMemberAreaStatisticsList() {
        // 统计用户
        // TODO @疯狂：可能得把每个省的用户，都查询出来，然后去 order 那边 in；因为要按照这些人为基础来计算；；用户规模量大可能不太好，但是暂时就先这样搞吧 = =
        Map<Integer, Integer> userCountMap = convertMap(memberStatisticsMapper.selectSummaryListByAreaId(),
                vo -> AreaUtils.getParentIdByType(vo.getAreaId(), AreaTypeEnum.PROVINCE),
                MemberAreaStatisticsRespBO::getUserCount, Integer::sum);
        // 统计订单
        Map<Integer, MemberAreaStatisticsRespBO> orderMap = convertMap(tradeOrderStatisticsService.getSummaryListByAreaId(),
                bo -> AreaUtils.getParentIdByType(bo.getAreaId(), AreaTypeEnum.PROVINCE),
                bo -> bo,
                (a, b) -> new MemberAreaStatisticsRespBO()
                        .setOrderCreateUserCount(formatInteger(a.getOrderCreateUserCount()) + formatInteger(b.getOrderCreateUserCount()))
                        .setOrderPayUserCount(formatInteger(a.getOrderPayUserCount()) + formatInteger(b.getOrderPayUserCount()))
                        .setOrderPayPrice(formatInteger(a.getOrderPayPrice()) + formatInteger(b.getOrderPayPrice())));
        // 拼接数据
        List<Area> areaList = AreaUtils.getByType(AreaTypeEnum.PROVINCE, area -> area);
        areaList.add(new Area().setId(null).setName("未知"));
        return MemberStatisticsConvert.INSTANCE.convertList(areaList, userCountMap, orderMap);
    }

    public Integer formatInteger(Integer data){
        Integer result = 0;
        if (data!=null){
            result =data;
        }
        return result;
    }

    @Override
    public DataComparisonRespVO<MemberAnalyseDataRespVO> getMemberAnalyseComparisonData(LocalDateTime beginTime, LocalDateTime endTime) {
        // 当前数据
        MemberAnalyseDataRespVO vo = getMemberAnalyseData(beginTime, endTime);
        // 对照数据
        LocalDateTime referenceEndDate = beginTime.minusDays(1); // 减少1天，防止出现时间重叠
        LocalDateTime referenceBeginDate = referenceEndDate.minus(Duration.between(beginTime, endTime));
        MemberAnalyseDataRespVO reference = getMemberAnalyseData(
                LocalDateTimeUtil.beginOfDay(referenceBeginDate), LocalDateTimeUtil.endOfDay(referenceEndDate));
        return new DataComparisonRespVO<>(vo, reference);
    }

    private MemberAnalyseDataRespVO getMemberAnalyseData(LocalDateTime beginTime, LocalDateTime endTime) {
        Integer rechargeUserCount = Optional.ofNullable(payWalletStatisticsService.getUserRechargeSummary(beginTime, endTime))
                .map(RechargeSummaryRespBO::getRechargeUserCount).orElse(0);
        return new MemberAnalyseDataRespVO()
                .setRegisterUserCount(memberStatisticsMapper.selectUserCount(beginTime, endTime))
                .setVisitUserCount(apiAccessLogStatisticsService.getUserCount(UserTypeEnum.MEMBER.getValue(), beginTime, endTime))
                .setRechargeUserCount(rechargeUserCount);
    }

    @Override
    public List<MemberSexStatisticsRespVO> getMemberSexStatisticsList() {
        return memberStatisticsMapper.selectSummaryListBySex();
    }

    @Override
    public List<MemberTerminalStatisticsRespVO> getMemberTerminalStatisticsList() {
        return memberStatisticsMapper.selectSummaryListByRegisterTerminal();
    }

    @Override
    public List<MemberRegisterCountRespVO> getMemberRegisterCountList(LocalDateTime beginTime, LocalDateTime endTime) {
        return memberStatisticsMapper.selectListByCreateTimeBetween(beginTime, endTime);
    }

    @Override
    public DataComparisonRespVO<MemberCountRespVO> getUserCountComparison() {
        // 今日时间范围
        LocalDateTime beginOfToday = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
        LocalDateTime endOfToday = LocalDateTimeUtil.endOfDay(beginOfToday);
        // 昨日时间范围
        LocalDateTime beginOfYesterday = LocalDateTimeUtil.beginOfDay(beginOfToday.minusDays(1));
        LocalDateTime endOfYesterday = LocalDateTimeUtil.endOfDay(beginOfYesterday);
        return new DataComparisonRespVO<MemberCountRespVO>()
                .setValue(getUserCount(beginOfToday, endOfToday))
                .setReference(getUserCount(beginOfYesterday, endOfYesterday));
    }

    @Override
    public PageResult<MemberVisitRespVO> getMemberVisitPage(MemberVisitPageReqVO pageReqVO) {
        PageResult<MemberVisitRespVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }

        Long total = 0L;
        List<MemberVisitRespVO> list = new ArrayList<>();
        //查询实时数据
        if (pageReqVO.getIsRealTime()==null || pageReqVO.getIsRealTime()){
            total = memberStatisticsMapper.selectMemberVisitCount(startTime, endTime);
            if (total.compareTo(0L)>0){
                Integer startIndex = null;
                if (pageReqVO.getPageSize()>0){
                    startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
                }
                list = memberStatisticsMapper.selectMemberVisitListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
                if (list==null){
                    list=new ArrayList<>();
                }else{
                    for(MemberVisitRespVO dd:list){
                        if (dd.getBirthday()!=null){
                            dd.setAge(Period.between(dd.getBirthday().toLocalDate(),LocalDateTime.now().toLocalDate()).getYears());
                        }

                        if (dd.getSex()==null){
                            dd.setSex(SexEnum.UNKNOWN.getSex());
                        }
                    }
                }
            }
        }else{
            //查询汇总数据
            total = memberStatisticsMapper.selectMemberVisitStatCount(startTime, endTime);
            if (total.compareTo(0L)>0){
                Integer startIndex = null;
                if (pageReqVO.getPageSize()>0){
                    startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
                }
                list = memberStatisticsMapper.selectMemberVisitStatListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
                if (list==null){
                    list=new ArrayList<>();
                }else{
                    for(MemberVisitRespVO dd:list){
                        if (dd.getBirthday()!=null){
                            dd.setAge(Period.between(dd.getBirthday().toLocalDate(),LocalDateTime.now().toLocalDate()).getYears());
                        }

                        if (dd.getSex()==null){
                            dd.setSex(SexEnum.UNKNOWN.getSex());
                        }
                    }
                }
            }
        }


        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }

    @Override
    public PageResult<MemberActionRespVO> getMemberActionPage(MemberActionPageReqVO pageReqVO) {
        PageResult<MemberActionRespVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }
        Long total = 0L;
        List<MemberActionRespVO> list = new ArrayList<>();
        //查询实时数据
        if (pageReqVO.getIsRealTime()==null || pageReqVO.getIsRealTime()){
            total = memberStatisticsMapper.selectMemberActionCount(startTime, endTime);
            if (total.compareTo(0L)>0){
                Integer startIndex = null;
                if (pageReqVO.getPageSize()>0){
                    startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
                }
                list = memberStatisticsMapper.selectMemberActionListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
                if (list==null){
                    list=new ArrayList<>();
                }else{
                    Map<String,Integer> newMapData = new HashMap<>();
                    List<MemberActionRespVO> newMemberList = memberStatisticsMapper.selectNewMemberActionListByCreateTimeBetween(startTime, endTime);
                    if (newMemberList!=null && newMemberList.size()>0){
                        for(MemberActionRespVO dd:newMemberList){
                            newMapData.put(dd.getTitle(),dd.getUv());
                        }
                    }

                    Map<String,Integer> newMapData2 = new HashMap<>();
                    List<MemberActionRespVO> newMemberList2 = memberStatisticsMapper.selectHintActionListByCreateTimeBetween(startTime, endTime);
                    if (newMemberList2!=null && newMemberList2.size()>0){
                        for(MemberActionRespVO dd:newMemberList2){
                            newMapData2.put(dd.getTitle(),dd.getPv());
                        }
                    }

                    for(MemberActionRespVO dd:list){
                        Integer oldUserCount =0 ;
                        Integer newUserCount = 0 ;
                        Integer clickCount = 0;
                        Integer totalUserCount = dd.getUv();
                        if (newMapData.containsKey(dd.getTitle())){
                            newUserCount = newMapData.get(dd.getTitle());
                        }
                        oldUserCount = totalUserCount-newUserCount;

                        //计算页面曝光点击率
                        String exposureRate = "";
                        if (newMapData2.containsKey(dd.getTitle())){
                            clickCount = newMapData2.get(dd.getTitle());
                            if ((dd.getPv()!=null && dd.getPv()>0) && (clickCount!=null)){
                                BigDecimal tmp01 = new BigDecimal(clickCount);
                                BigDecimal tmp02 = new BigDecimal(dd.getPv());
                                exposureRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).toPlainString();
                            }
                        }
                        dd.setClickCount(clickCount);
                        dd.setExposureRate(exposureRate);
                        dd.setNewUserCount(newUserCount);
                        dd.setOldUserCount(oldUserCount);
                    }
                }
            }
        }else{
            //查询汇总数据
            total = memberStatisticsMapper.selectMemberActionStatCount(startTime, endTime);
            if (total.compareTo(0L)>0){
                Integer startIndex = null;
                if (pageReqVO.getPageSize()>0){
                    startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
                }
                list = memberStatisticsMapper.selectMemberActionStatListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
                if (list==null){
                    list=new ArrayList<>();
                }else{
                    Map<String,Integer> newMapData = new HashMap<>();
                    List<MemberActionRespVO> newMemberList = memberStatisticsMapper.selectNewMemberActionStatListByCreateTimeBetween(startTime, endTime);
                    if (newMemberList!=null && newMemberList.size()>0){
                        for(MemberActionRespVO dd:newMemberList){
                            newMapData.put(dd.getTitle(),dd.getUv());
                        }
                    }

                    for(MemberActionRespVO dd:list){
                        Integer oldUserCount = 0 ;
                        Integer newUserCount = 0 ;
                        Integer clickCount = dd.getClickCount();
                        Integer totalUserCount = dd.getUv();
                        if (newMapData.containsKey(dd.getTitle())){
                            newUserCount = newMapData.get(dd.getTitle());
                        }
                        oldUserCount = totalUserCount-newUserCount;

                        //计算页面曝光点击率
                        String exposureRate = "";
                        if ((dd.getPv()!=null && dd.getPv()>0) && (clickCount!=null)){
                            BigDecimal tmp01 = new BigDecimal(clickCount);
                            BigDecimal tmp02 = new BigDecimal(dd.getPv());
                            exposureRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).toPlainString();
                        }
                        dd.setClickCount(clickCount);
                        dd.setExposureRate(exposureRate);
                        dd.setNewUserCount(newUserCount);
                        dd.setOldUserCount(oldUserCount);
                    }
                }
            }
        }

        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }

    private MemberCountRespVO getUserCount(LocalDateTime beginTime, LocalDateTime endTime) {
        return new MemberCountRespVO()
                .setRegisterUserCount(memberStatisticsMapper.selectUserCount(beginTime, endTime))
                .setVisitUserCount(apiAccessLogStatisticsService.getUserCount(UserTypeEnum.MEMBER.getValue(), beginTime, endTime));
    }

}
