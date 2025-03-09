package cn.iocoder.yudao.server.controller.job;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.module.infra.api.logger.ApiAccessLogApi;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogUpdateReqDTO;
import cn.iocoder.yudao.server.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LogJob implements JobHandler {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private ApiAccessLogApi apiAccessLogApi;

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    private static final String REDIS_KEY_LOG_LIST = "REDIS_KEY_LOG_LIST";

    private static final Long MAX_COUNT = 1000L;

    //是否正在进行中
    private static Boolean isDo = false;

    @Override
    public String execute(String param) throws Exception {
        log.info("log job start");
        Integer maxCount = MAX_COUNT.intValue();
        if (StringUtils.isNotEmpty(param)) {
            maxCount = Integer.valueOf(param);
        }
        if (isDo) {
            return "";
        }
        isDo = true;
        try {
            if (redisTemplate.hasKey(REDIS_KEY_LOG_LIST)) {
                //新增记录
                List<ApiAccessLogCreateReqDTO> addList = new ArrayList<>();
                //修改记录
                List<ApiAccessLogUpdateReqDTO> updateList = new ArrayList<>();

                Long listSize = redisTemplate.opsForList().size(REDIS_KEY_LOG_LIST);
                log.info("log size:" + listSize);

                //设置每次读取记录数
                Integer totalCount = 0;
                if (listSize.compareTo(Long.valueOf(maxCount)) > 0) {
                    totalCount = maxCount;
                } else {
                    totalCount = listSize.intValue();
                }

                for (int i = 0; i < totalCount; i++) {
                    String content = redisTemplate.opsForList().rightPop(REDIS_KEY_LOG_LIST);
                    if (StringUtils.isNotEmpty(content)) {
                        String[] arr = content.split("\\|");
                        String type = "";
                        String traceId = "";
                        if (arr.length > 0) {
                            type = arr[0];
                        }

                        if (arr.length > 1) {
                            traceId = arr[1];
                        }

                        if (type.equalsIgnoreCase("0")) {
                            //API 日志
                            String userId = arr[2];
                            String requestUrl = arr[3];
                            String requestParams = arr[4];
                            String requestMethod = arr[5];
                            String userAgent = arr[6];
                            String userIp = arr[7];
                            String beginTimeStr = arr[8];
                            String endTimeStr = arr[9];
                            String duration = arr[10];
                            String mobileModel = arr[11];
                            String bizId = "";
                            if (arr.length > 12) {
                                bizId = arr[12];
                            }
                            String count = "";
                            if (arr.length > 13) {
                                count = arr[13];
                            }

                            ApiAccessLogCreateReqDTO accessLog = new ApiAccessLogCreateReqDTO();
                            accessLog.setApplicationName(applicationName);
                            if (StringUtils.isNotEmpty(userId)) {
                                accessLog.setUserId(Long.valueOf(userId));
                            }
                            accessLog.setUserType(UserTypeEnum.MEMBER.getValue());
                            accessLog.setRequestUrl(requestUrl);
                            accessLog.setRequestParams(requestParams);
                            accessLog.setRequestMethod(requestMethod);
                            accessLog.setUserAgent(userAgent);
                            accessLog.setUserIp(userIp);
                            // 持续时间
                            accessLog.setTraceId(traceId);
                            accessLog.setBeginTime(CommonUtils.toFormatLocalDateTime(beginTimeStr, "yyyy-MM-dd HH:mm:ss"));
                            accessLog.setEndTime(CommonUtils.toFormatLocalDateTime(endTimeStr, "yyyy-MM-dd HH:mm:ss"));
                            accessLog.setDuration(Integer.valueOf(duration));
                            accessLog.setResultCode(0);
                            accessLog.setResultMsg("");
                            accessLog.setType(0);
                            accessLog.setMobileModel(mobileModel);
                            accessLog.setBizId(bizId);
                            if (StringUtils.isNotEmpty(count)) {
                                accessLog.setCount(Integer.valueOf(count));
                            }
                            if (StringUtils.isNotEmpty(userAgent)) {
                                accessLog.setBrowser(ServletUtils.getBrowser(userAgent));
                            }
                            addList.add(accessLog);
                        } else if (type.equalsIgnoreCase("1") || type.equalsIgnoreCase("5")) {
                            //新增页面访问或按钮点击
                            String userId = arr[2];
                            String requestUrl = arr[3];
                            String title = arr[4];
                            String userAgent = arr[5];
                            String userIp = arr[6];
                            String beginTimeStr = arr[7];
                            String mobileModel = arr[8];
                            String groupName = "";
                            if (arr.length > 9) {
                                groupName = arr[9];
                            }
                            String bizId = "";
                            if (arr.length > 10) {
                                bizId = arr[10];
                            }

                            ApiAccessLogCreateReqDTO accessLog = new ApiAccessLogCreateReqDTO();
                            accessLog.setApplicationName(applicationName);
                            if (StringUtils.isNotEmpty(userId)) {
                                accessLog.setUserId(Long.valueOf(userId));
                            }
                            accessLog.setUserType(UserTypeEnum.MEMBER.getValue());
                            accessLog.setRequestUrl(requestUrl);
                            accessLog.setTitle(title);
                            accessLog.setUserAgent(userAgent);
                            accessLog.setUserIp(userIp);
                            // 持续时间
                            accessLog.setBeginTime(CommonUtils.toFormatLocalDateTime(beginTimeStr, "yyyy-MM-dd HH:mm:ss"));
                            accessLog.setTraceId(traceId);
                            accessLog.setResultCode(0);
                            accessLog.setResultMsg("");
                            accessLog.setType(Integer.valueOf(type));
                            accessLog.setMobileModel(mobileModel);
                            accessLog.setGroupName(groupName);
                            accessLog.setBizId(bizId);
                            if (StringUtils.isNotEmpty(userAgent)) {
                                accessLog.setBrowser(ServletUtils.getBrowser(userAgent));
                            }
                            addList.add(accessLog);
                        } else if (type.equalsIgnoreCase("2")) {
                            //修改
                            String endTimeStr = arr[2];
                            if (StringUtils.isNotEmpty(endTimeStr)) {
                                ApiAccessLogUpdateReqDTO updateReqDTO = new ApiAccessLogUpdateReqDTO();
                                updateReqDTO.setTraceId(traceId);
                                updateReqDTO.setEndTime(endTimeStr);
                                updateList.add(updateReqDTO);
                            }
                        }
                    } else {
                        break;
                    }
                }

                if (addList.size() > 0) {
                    apiAccessLogApi.addBatchApiAccessLog(addList);
                }

                if (updateList.size() > 0) {
                    apiAccessLogApi.updateBatchApiAccessLog(updateList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        isDo = false;
        log.info("log job end");
        return "操作成功";
    }
}
