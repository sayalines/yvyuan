package cn.iocoder.yudao.module.member.job;

import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.module.member.controller.admin.memberactionlog.vo.MemberActionLogSaveReqVO;
import cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo.MemberVisitLogSaveReqVO;
import cn.iocoder.yudao.module.member.controller.admin.productvisitlog.vo.ProductVisitLogSaveReqVO;
import cn.iocoder.yudao.module.member.dal.mysql.log.LogMapper;
import cn.iocoder.yudao.module.member.service.memberactionlog.MemberActionLogService;
import cn.iocoder.yudao.module.member.service.membervisitlog.MemberVisitLogService;
import cn.iocoder.yudao.module.member.service.productvisitlog.ProductVisitLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StatLogJob implements JobHandler {


    @Resource
    private LogMapper logMapper;

    @Resource
    private MemberVisitLogService memberVisitLogService;

    @Resource
    private MemberActionLogService memberActionLogService;

    @Resource
    private ProductVisitLogService productVisitLogService;

    @Override
    public String execute(String param) throws Exception {
        log.info("stat log job start");
        List<String> dateList = logMapper.getDateList();
        if (dateList!=null && dateList.size()>0){
            for(String ss : dateList){
                LocalDateTime statTime = toFormatLocalDateTime(ss,"yyyy-MM-dd");
                if (!DateUtils.isToday(statTime)){
                    log.info("stat log time :"+ss);
                    LocalDateTime beginTime = statTime;
                    LocalDateTime endTime = getEndTime(ss);

                    //汇总访客信息
                    statMemberVisitData(beginTime,endTime);

                    //汇总访客行为
                    statMemberActionData(beginTime,endTime);

                    //汇总产品访问
                    statProductVisitData(beginTime,endTime);
                }
            }
        }
        log.info("stat log job end");
        return "操作成功";
    }

    public void statMemberVisitData(LocalDateTime beginTime,LocalDateTime endTime){
        //判断如果没有汇总过数据，就汇总数据
        Long recordCount = logMapper.getMemberVisitCount(beginTime,endTime);
        if (recordCount==null || recordCount.equals(0L)){
            List<Map<String,Object>> list = logMapper.getMemberVisitList(beginTime,endTime);
            if (list!=null && list.size()>0){
                for(Map<String,Object> mapData :list){
                    MemberVisitLogSaveReqVO logDO = new MemberVisitLogSaveReqVO();
                    logDO.setStatTime(beginTime);

                    String fieldName = "user_id";
                    String content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setUserId(Long.valueOf(content));
                    }

                    fieldName = "user_ip";
                    content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setUserIp(content);
                    }

                    fieldName = "mobile_model";
                    content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setMobileModel(content);
                    }

                    fieldName = "min_time";
                    logDO.setMinTime(formatLocalDateTime(mapData.get(fieldName)));

                    fieldName = "max_time";
                    logDO.setMaxTime(formatLocalDateTime(mapData.get(fieldName)));

                    memberVisitLogService.createVisitLog(logDO);
                }
            }
        }
    }

    public void statMemberActionData(LocalDateTime beginTime,LocalDateTime endTime){
        //判断如果没有汇总过数据，就汇总数据
        Long recordCount = logMapper.getMemberActionCount(beginTime,endTime);
        if (recordCount==null || recordCount.equals(0L)){
            List<Map<String,Object>> list = logMapper.getMemberActionList(beginTime,endTime);
            if (list!=null && list.size()>0){
                List<Map<String,Object>> hitList = logMapper.getMemberActionHitList(beginTime,endTime);
                Map<String,Integer> hitMap = new HashMap<>();
                if (hitList!=null && hitList.size()>0){
                    for(Map<String,Object> data :hitList){
                        String tmpUserId = "";
                        String tmpTitle = "";
                        Integer tmpValue = 0;
                        String fieldName = "user_id";
                        String content = formatStr(data.get(fieldName));
                        if (StringUtils.isNotEmpty(content)){
                            tmpUserId = content;
                        }

                        fieldName = "title";
                        content = formatStr(data.get(fieldName));
                        if (StringUtils.isNotEmpty(content)){
                            tmpTitle = content;
                        }

                        fieldName = "pv";
                        content = formatStr(data.get(fieldName));
                        if (StringUtils.isNotEmpty(content)){
                            tmpValue = Integer.valueOf(content);
                        }

                        hitMap.put(tmpUserId+"_"+tmpTitle,tmpValue);
                    }
                }
                for(Map<String,Object> mapData :list){
                    String userId = "";
                    String title = "";

                    MemberActionLogSaveReqVO logDO = new MemberActionLogSaveReqVO();
                    logDO.setStatTime(beginTime);

                    String fieldName = "user_id";
                    String content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setUserId(Long.valueOf(content));
                        userId = content;
                    }

                    fieldName = "title";
                    content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setTitle(content);
                        title = content;
                    }

                    fieldName = "pv";
                    content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setVisitCount(Integer.valueOf(content));
                    }

                    fieldName = "min_time";
                    logDO.setMinTime(formatLocalDateTime(mapData.get(fieldName)));

                    fieldName = "max_time";
                    logDO.setMaxTime(formatLocalDateTime(mapData.get(fieldName)));

                    //获取点击数
                    if (hitMap.containsKey(userId+"_"+title)){
                        logDO.setClickCount(hitMap.get(userId+"_"+title));
                    }

                    memberActionLogService.createActionLog(logDO);
                }
            }
        }
    }

    public void statProductVisitData(LocalDateTime beginTime,LocalDateTime endTime){
        //判断如果没有汇总过数据，就汇总数据
        Long recordCount = logMapper.getProductVisitCount(beginTime,endTime);
        if (recordCount==null || recordCount.equals(0L)){
            List<Map<String,Object>> list = logMapper.getProductVisitList(beginTime,endTime);
            if (list!=null && list.size()>0){
                List<Map<String,Object>> cartList = logMapper.getProductCartList(beginTime,endTime);
                Map<String,Integer> cartMap = new HashMap<>();
                if (cartList!=null && cartList.size()>0){
                    for(Map<String,Object> data :cartList){
                        String tmpUserId = "";
                        String tmpSpuId = "";
                        Integer tmpValue = 0;
                        String fieldName = "user_id";
                        String content = formatStr(data.get(fieldName));
                        if (StringUtils.isNotEmpty(content)){
                            tmpUserId = content;
                        }

                        fieldName = "spu_id";
                        content = formatStr(data.get(fieldName));
                        if (StringUtils.isNotEmpty(content)){
                            tmpSpuId = content;
                        }

                        fieldName = "cart_count";
                        content = formatStr(data.get(fieldName));
                        if (StringUtils.isNotEmpty(content)){
                            tmpValue = Integer.valueOf(content);
                        }

                        cartMap.put(tmpUserId+"_"+tmpSpuId,tmpValue);
                    }
                }

                for(Map<String,Object> mapData :list){
                    String userId = "";
                    String spuId = "";

                    ProductVisitLogSaveReqVO logDO = new ProductVisitLogSaveReqVO();
                    logDO.setStatTime(beginTime);

                    String fieldName = "user_id";
                    String content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setUserId(Long.valueOf(content));
                        userId = content;
                    }

                    fieldName = "spu_id";
                    content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setSpuId(Long.valueOf(content));
                        spuId = content;
                    }

                    fieldName = "spu_name";
                    content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setSpuName(content);
                    }

                    fieldName = "hit_count";
                    content = formatStr(mapData.get(fieldName));
                    if (StringUtils.isNotEmpty(content)){
                        logDO.setHitCount(Integer.valueOf(content));
                    }

                    fieldName = "min_time";
                    logDO.setMinTime(formatLocalDateTime(mapData.get(fieldName)));

                    fieldName = "max_time";
                    logDO.setMaxTime(formatLocalDateTime(mapData.get(fieldName)));

                    //加购件数
                    if (cartMap.containsKey(userId+"_"+spuId)){
                        logDO.setCartCount(cartMap.get(userId+"_"+spuId));
                    }

                    productVisitLogService.createProductVisitLog(logDO);
                }
            }
        }
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public LocalDateTime toFormatLocalDateTime(String dateTime, String formatter){
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

    public String formatStr(Object value){
        String result = "";
        if (value!=null){
            result = String.valueOf(value);
        }
        return result;
    }

    public Long formatLong(Object value){
        Long result = null;
        if (value!=null){
            result = (Long) value;
        }
        return result;
    }

    public LocalDateTime formatLocalDateTime(Object value){
        LocalDateTime result = null;
        if (value!=null){
            result = (LocalDateTime) value;
        }
        return result;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public LocalDateTime getEndTime(String dateTime){
        LocalDateTime result = null;
        if (StringUtils.isNotEmpty(dateTime)){
            dateTime = dateTime+" 23:59:59";
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            result = LocalDateTime.parse(dateTime,df);
        }
        return result;
    }


}
