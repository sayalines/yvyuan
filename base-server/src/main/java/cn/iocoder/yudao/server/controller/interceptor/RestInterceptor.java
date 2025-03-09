package cn.iocoder.yudao.server.controller.interceptor;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.infra.api.logger.ApiAccessLogApi;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.server.service.RedisService;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;

public class RestInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(RestInterceptor.class);
    @Resource
    private ApiAccessLogApi apiAccessLogApi;
    @Resource
    private RedisService redisService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

//    /**
//     * 缓存文件目录
//     */
//    @Value("${cache.file.path}")
//    private String cacheDirectory;

    // 使用ConcurrentHashMap来存储请求的开始时间，以便在请求结束时可以访问
    private static final ConcurrentHashMap<String, Long> startTimeMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> tradeMap = new ConcurrentHashMap<>();
    public static final String LOGIN_TOKEN_KEY = "Authorization";
    public static final String PREFIX_USER_ID_TOKEN  = "PREFIX_USER_ID_TOKEN_";
    private static final String REDIS_KEY_LOG_LIST = "REDIS_KEY_LOG_LIST";
    public static final String PLAT_TRACE_ID  = "PLAT_TRACE_ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前请求的URL作为key
        String url = request.getRequestURL().toString();
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        // 将开始时间存入map中，以URL为key
        startTimeMap.put(url, startTime);
        //跟踪编号
        String traceId = startTime+""+ RandomUtil.randomNumbers(3);
        tradeMap.put(url, traceId);
        request.setAttribute(PLAT_TRACE_ID,traceId);
        return true; // 继续执行下一个拦截器
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在这里可以执行一些处理，但通常不会记录结束时间，因为响应体可能还未完全写入
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 获取请求的URL
        String url = request.getRequestURL().toString();
        // 获取开始时间
        Long startTime = startTimeMap.remove(url);
        String traceId = tradeMap.remove(url);
        if (startTime != null) {
            // 计算执行时间
            long endTime = System.currentTimeMillis();

            // 提前获得参数，避免 XssFilter 过滤处理
            Map<String, String> queryString = ServletUtils.getParamMap(request);
            String requestBody = ServletUtils.isJsonRequest(request) ? ServletUtils.getBody(request) : null;
            try{

                String type = "0";
                String userId = CommonUtils.formatLong(getUserId(request));
                String requestUrl = CommonUtils.formatString(request.getRequestURI());
                String requestParams = CommonUtils.formatString(toJsonString(MapUtil.<String, Object>builder().put("query", queryString).put("body", requestBody).build()));
                String requestMethod = CommonUtils.formatString(request.getMethod());
                String userAgent = CommonUtils.formatString(ServletUtils.getUserAgent(request));
                String userIp = CommonUtils.formatString(ServletUtils.getClientIP(request));

                LocalDateTime beginTime2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
                String beginTimeStr = CommonUtils.formatLocalDateTime(beginTime2,"yyyy-MM-dd HH:mm:ss");
                LocalDateTime endTime2 =LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
                String endTimeStr = CommonUtils.formatLocalDateTime(endTime2,"yyyy-MM-dd HH:mm:ss");
                String duration = String.valueOf((int) LocalDateTimeUtil.between(beginTime2, endTime2, ChronoUnit.MILLIS));
                String mobileModel = CommonUtils.getMobileModel(userAgent);
                String bizId="";
                String count="";
                if (requestUrl.equalsIgnoreCase("/rest/api/cart/add") || requestUrl.equalsIgnoreCase("/rest/api/cart/now-buy")){
                    //读取添加购物车接口中的规格ID，用于统计
                    if (queryString.containsKey("skuId")){
                        bizId =queryString.get("skuId");
                    }
                    if (queryString.containsKey("count")){
                        count = String.valueOf(queryString.get("count"));
                    }
                }


                String content = type+"|"+traceId+"|"+userId+"|"+requestUrl+"|"+requestParams+"|"
                        +requestMethod+"|"+userAgent+"|"+userIp+"|"+beginTimeStr+"|"+endTimeStr+"|"+duration+"|"+mobileModel+"|"+bizId+"|"+count;
                redisTemplate.opsForList().leftPush(REDIS_KEY_LOG_LIST,content);


//                FileUtils.writeLog(cacheDirectory,content);
//                ApiAccessLogCreateReqDTO accessLog = new ApiAccessLogCreateReqDTO();
//                accessLog.setApplicationName(applicationName);
//                accessLog.setUserId(getUserId(request));
//                accessLog.setUserType(UserTypeEnum.MEMBER.getValue());
//                accessLog.setRequestUrl(request.getRequestURI());
//                Map<String, Object> requestParams = MapUtil.<String, Object>builder().put("query", queryString).put("body", requestBody).build();
//                accessLog.setRequestParams(toJsonString(requestParams));
//                accessLog.setRequestMethod(request.getMethod());
//                accessLog.setUserAgent(ServletUtils.getUserAgent(request));
//                accessLog.setUserIp(ServletUtils.getClientIP(request));
//                // 持续时间
//                accessLog.setBeginTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
//                accessLog.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
//                accessLog.setDuration((int) LocalDateTimeUtil.between(accessLog.getBeginTime(), accessLog.getEndTime(), ChronoUnit.MILLIS));
//                accessLog.setTraceId(TracerUtils.getTraceId());
//                accessLog.setResultCode(0);
//                accessLog.setResultMsg("");
//                accessLog.setType(0);
//                accessLog.setMobileModel(CommonUtils.getMobileModel(accessLog.getUserAgent()));
//                apiAccessLogApi.createApiAccessLog(accessLog);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Long getUserId(HttpServletRequest request){
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            Object obj = redisService.get(PREFIX_USER_ID_TOKEN + token);
            if (obj!=null){
                return Long.valueOf(String.valueOf(obj));
            }
        }else {
            String tsCustomerId = request.getHeader("tsCustomerId");
            if (StringUtils.isNotEmpty(tsCustomerId)) {
                return Long.valueOf(tsCustomerId);
            }
        }
        return null;
    }

}
