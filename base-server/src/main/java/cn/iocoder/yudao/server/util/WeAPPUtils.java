package cn.iocoder.yudao.server.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序接口
 * @author wangqing
 *
 */
public class WeAPPUtils {
    private static Logger logger = LoggerFactory.getLogger(WeAPPUtils.class);

    private static final String  accessTokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";

//    private static final String  userLoginUrl="https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

    /**
     * 获取小程序token
     * @param appId
     * @param appSecret
     * @return
     */

    public static String getAccessToken(String appId,String appSecret) {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret)){
            return "";
        }
        Integer errorCount = 3;
        String url=accessTokenUrl.replace("APPID", appId).replace("SECRET", appSecret);
        String accessToken="";
        while (errorCount > 0){
            try {
                String result=WeHttpClientUtils.get(url);
                JSONObject jsonObject= JSON.parseObject(result);
                accessToken=jsonObject.getString("access_token");
                logger.info("getAccessToken-result:{}",accessToken);
                break;
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            errorCount=errorCount-1;
        }

        return accessToken;
    }

//    /**
//     * 获取用户openid
//     * @param code
//     * @return
//     */
//    public static String getOpenId(String appId,String appSecret,String code) {
//        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(code)){
//            return null;
//        }
//        String url=userLoginUrl.replace("APPID", appId).replace("SECRET", appSecret).replace("JSCODE", code);
//        try {
//            String result=WeHttpClientUtils.get(url);
//            logger.info("getLoginInfo-result:{}",result);
//            if (StringUtils.isNotEmpty(result)){
//                JSONObject jsonObject= JSON.parseObject(result);
//                if (jsonObject.containsKey("openid")){
//                    return jsonObject.getString("openid");
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//
//        return null;
//    }

    /**
     * 发送消息模板
     * @param token
     * @param templateId
     * @param openId
     * @param map
     * @return
     */
    public static Integer sendSms(String token, String templateId,String page, String openId, Map<String,String> map){
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(token)){
            return 0;
        }

        try{
            WxMessageTemplate temp = new WxMessageTemplate();
            temp.setTouser(openId);
            temp.setTemplate_id(templateId);
            temp.setPage(page);

            Map<String, TemplateData> m = new HashMap<>();
            for(String key:map.keySet()){
                TemplateData templateData = new TemplateData();
                templateData.setValue(map.get(key));
                m.put(key, templateData);
            }
            temp.setData(m);

            String jsonString = JSONObject.toJSONString(temp);
            logger.info("模板消息jsonString：{}", jsonString);

            String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + token;
            HttpRequest request = new HttpRequest(url);
            String result = request.postData("", jsonString);
            logger.info("模板消息发送结果：{}", result);
            if (StringUtils.isNotEmpty(result)){
                JSONObject resJson = JSONObject.parseObject(result);
                if (resJson.containsKey("errcode")){
                    String errcode = resJson.getString("errcode");
                    logger.info("errcode：{}", errcode);
                    if (errcode.equalsIgnoreCase("40001") || errcode.equalsIgnoreCase("40014")){
                        return 2;
                    }
                }
            }
            return 1;
        }catch (Exception e){
            logger.error("sendSms-error:{}",e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
