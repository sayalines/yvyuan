package cn.iocoder.yudao.module.infra.service.logger;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogUpdateReqDTO;
import cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.logger.ApiAccessLogDO;
import cn.iocoder.yudao.module.infra.dal.mysql.logger.ApiAccessLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * API 访问日志 Service 实现类
 *
 * @author 商城管理系统
 */
@Slf4j
@Service
@Validated
public class ApiAccessLogServiceImpl implements ApiAccessLogService {

    @Resource
    private ApiAccessLogMapper apiAccessLogMapper;

    @Override
    public Long createApiAccessLog(ApiAccessLogCreateReqDTO createDTO) {
        ApiAccessLogDO apiAccessLog = BeanUtils.toBean(createDTO, ApiAccessLogDO.class);
        apiAccessLogMapper.insert(apiAccessLog);
        return apiAccessLog.getId();
    }

    @Override
    public void addBatchApiAccessLog(List<ApiAccessLogCreateReqDTO> list) {
        if (list.size()>0){
            List<ApiAccessLogDO> entityList = new ArrayList<>();
            for(ApiAccessLogCreateReqDTO dd:list){
                entityList.add(BeanUtils.toBean(dd, ApiAccessLogDO.class));
                //每10条提交一次
                if (entityList.size()>=10){
                    apiAccessLogMapper.insertBatch(entityList);
                    entityList.clear();
                }
            }
           if (entityList.size()>0){
               apiAccessLogMapper.insertBatch(entityList);
           }
        }
    }

    @Override
    public void updateApiAccessLog(String traceId,String endTime) {
        ApiAccessLogDO entity = apiAccessLogMapper.selectOne(ApiAccessLogDO::getTraceId,traceId);
        if (entity!=null){
            entity.setEndTime(toFormatLocalDateTime(endTime,"yyyy-MM-dd HH:mm:ss"));
            entity.setDuration((int) LocalDateTimeUtil.between(entity.getBeginTime(), entity.getEndTime(), ChronoUnit.MILLIS));
            apiAccessLogMapper.updateById(entity);
        }
    }

    @Override
    public void updateBatchApiAccessLog(List<ApiAccessLogUpdateReqDTO> list) {
        if (list.size()>0){
            List<ApiAccessLogDO> entityList = new ArrayList<>();
            for(ApiAccessLogUpdateReqDTO dd:list){
                ApiAccessLogDO entity = apiAccessLogMapper.selectOne(ApiAccessLogDO::getTraceId,dd.getTraceId());
                if (entity!=null){
                    if (StringUtils.isNotEmpty(dd.getEndTime())){
                        entity.setEndTime(toFormatLocalDateTime(dd.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
                        entity.setDuration((int) LocalDateTimeUtil.between(entity.getBeginTime(), entity.getEndTime(), ChronoUnit.MILLIS));
                        entityList.add(entity);
                    }

                    //每10条提交一次
                    if (entityList.size()>=10){
                        apiAccessLogMapper.updateBatch(entityList);
                        entityList.clear();
                    }
                }
            }
            if (entityList.size()>0){
                apiAccessLogMapper.updateBatch(entityList);
            }
        }
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public static LocalDateTime toFormatLocalDateTime(String dateTime,String formatter){
        LocalDateTime result = null;
        if (StringUtils.isNotEmpty(dateTime)){
            if (StringUtils.isEmpty(formatter)){
                formatter = "yyyy-MM-dd HH:mm:ss";
            }
            if (formatter.equalsIgnoreCase("yyyy-MM-dd")){
                dateTime = dateTime+" 00:00:00";
                formatter="yyyy-MM-dd HH:mm:ss";
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
            result = LocalDateTime.parse(dateTime,df);
        }
        return result;
    }


    @Override
    public PageResult<ApiAccessLogDO> getApiAccessLogPage(ApiAccessLogPageReqVO pageReqVO) {
        return apiAccessLogMapper.selectPage(pageReqVO);
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Integer cleanAccessLog(Integer exceedDay, Integer deleteLimit) {
        int count = 0;
        LocalDateTime expireDate = LocalDateTime.now().minusDays(exceedDay);
        expireDate = expireDate.toLocalDate().atStartOfDay();
        // 循环删除，直到没有满足条件的数据
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            int deleteCount = apiAccessLogMapper.deleteByCreateTimeLt(expireDate, deleteLimit);
            count += deleteCount;
            // 达到删除预期条数，说明到底了
            if (deleteCount < deleteLimit) {
                break;
            }
        }
        return count;
    }

}
