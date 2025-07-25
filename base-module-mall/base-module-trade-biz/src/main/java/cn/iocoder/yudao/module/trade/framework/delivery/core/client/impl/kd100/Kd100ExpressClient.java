package cn.iocoder.yudao.module.trade.framework.delivery.core.client.impl.kd100;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.trade.framework.delivery.config.TradeExpressProperties;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.ExpressClient;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.ExpressTrackQueryReqDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.ExpressTrackRespDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.kd100.Kd100ExpressQueryReqDTO;
import cn.iocoder.yudao.module.trade.framework.delivery.core.client.dto.kd100.Kd100ExpressQueryRespDTO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.EXPRESS_API_QUERY_ERROR;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.EXPRESS_API_QUERY_FAILED;
import static cn.iocoder.yudao.module.trade.framework.delivery.core.client.convert.ExpressQueryConvert.INSTANCE;

/**
 * 快递 100 客户端
 *
 * @author jason
 */
@Slf4j
@AllArgsConstructor
public class Kd100ExpressClient implements ExpressClient {

    private static final String REAL_TIME_QUERY_URL = "https://poll.kuaidi100.com/poll/query.do";

    private final RestTemplate restTemplate;
    private final TradeExpressProperties.Kd100Config config;

    /**
     * 查询快递轨迹
     *
     * @see <a href="https://api.kuaidi100.com/debug-tool/query/">接口文档</a>
     *
     * @param reqDTO 查询请求参数
     * @return 快递轨迹
     */
    @Override
    public List<ExpressTrackRespDTO> getExpressTrackList(ExpressTrackQueryReqDTO reqDTO) {
        // 发起请求
        Kd100ExpressQueryReqDTO requestDTO = INSTANCE.convert2(reqDTO)
                .setExpressCode(reqDTO.getExpressCode().toLowerCase());
        Kd100ExpressQueryRespDTO respDTO = httpRequest(REAL_TIME_QUERY_URL, requestDTO);

        // 处理结果
        if (Objects.equals("false", respDTO.getResult())) {
            log.info("respDTO:{}",respDTO);
            return Collections.emptyList();
//            throw exception(EXPRESS_API_QUERY_FAILED, respDTO.getMessage());
        }
        if (CollUtil.isEmpty(respDTO.getTracks())) {
            return Collections.emptyList();
        }
        return INSTANCE.convertList2(respDTO.getTracks());
    }

    /**
     * 快递 100 API 请求
     *
     * @param url 请求 url
     * @param req 对应请求的请求参数
     */
    private Kd100ExpressQueryRespDTO httpRequest(String url, Kd100ExpressQueryReqDTO req) {
        Kd100ExpressQueryRespDTO respDTO = new Kd100ExpressQueryRespDTO();
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 请求体
        String param = JsonUtils.toJsonString(req);
        String sign = generateReqSign(param, config.getKey(), config.getCustomer()); // 签名
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("customer", config.getCustomer());
        requestBody.add("sign", sign);
        requestBody.add("param", param);
        log.debug("[httpRequest][请求参数({})]", requestBody);

        // 发送请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        log.debug("[httpRequest][的响应结果({})]", responseEntity);
        // 处理响应
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw exception(EXPRESS_API_QUERY_ERROR);
        }
        try{
            String result = responseEntity.getBody();
            if (!StringUtils.isEmpty(result)){
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.containsKey("state")){
                    respDTO.setState(jsonObject.getString("state"));
                }
                if (jsonObject.containsKey("com")){
                    respDTO.setExpressCompanyCode(jsonObject.getString("com"));
                }
                if (jsonObject.containsKey("nu")){
                    respDTO.setLogisticsNo(jsonObject.getString("nu"));
                }
                if (jsonObject.containsKey("result")){
                    respDTO.setResult(jsonObject.getString("result"));
                }
                if (jsonObject.containsKey("message")){
                    respDTO.setMessage(jsonObject.getString("message"));
                }
                if (jsonObject.containsKey("data")){
                    List<Kd100ExpressQueryRespDTO.ExpressTrack> tracks = new ArrayList<>();
                    JSONArray array = jsonObject.getJSONArray("data");
                    for(int i=0;i<array.size();i++){
                        JSONObject sub = array.getJSONObject(i);
                        Kd100ExpressQueryRespDTO.ExpressTrack expressTrack = new Kd100ExpressQueryRespDTO.ExpressTrack();
                        if (sub.containsKey("time")){
                            expressTrack.setTime(toFormatLocalDateTime(sub.getString("time"),"yyyy-MM-dd HH:mm:ss"));
                        }
                        if (sub.containsKey("context")){
                            expressTrack.setContext(sub.getString("context"));
                        }
                        tracks.add(expressTrack);
                    }
                    respDTO.setTracks(tracks);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respDTO;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public static LocalDateTime toFormatLocalDateTime(String dateTime, String formatter){
        LocalDateTime result = null;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dateTime)){
            if (org.apache.commons.lang3.StringUtils.isEmpty(formatter)){
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

    private String generateReqSign(String param, String key, String customer) {
        String plainText = String.format("%s%s%s", param, key, customer);
        return HexUtil.encodeHexStr(DigestUtil.md5(plainText), false);
    }

}
