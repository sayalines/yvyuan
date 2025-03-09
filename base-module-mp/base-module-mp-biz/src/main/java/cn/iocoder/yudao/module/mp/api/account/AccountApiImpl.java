package cn.iocoder.yudao.module.mp.api.account;

import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.mp.account.AccountApi;
import cn.iocoder.yudao.module.mp.convert.account.MpAccountConvert;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.service.account.MpAccountService;
import cn.iocoder.yudao.module.mp.util.HttpRequest;
import cn.iocoder.yudao.module.mp.util.WeHttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Validated
@Slf4j
public class AccountApiImpl implements AccountApi {

    private static final String  accessTokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    private static final String  shortLinkUrl="https://api.weixin.qq.com/wxa/genwxashortlink?access_token=ACCESS_TOKEN";
    private static final String  qrcodeUrl="https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=ACCESS_TOKEN";

    @Resource
    private MpAccountService mpAccountService;
    @Resource
    private FileApi fileApi;


    @Override
    public String getAccessToken(Boolean isRefresh) {
        MpAccountDO accountDO = mpAccountService.getAccount(1L);
        if (accountDO!=null){
            String appId = accountDO.getAppId();
            String appSecret = accountDO.getAppSecret();
            String accessToken = accountDO.getToken();
            LocalDateTime endTokenTime = accountDO.getExpireDate();
            if (isRefresh || StringUtils.isBlank(accessToken) || endTokenTime.compareTo(LocalDateTime.now())<=0) {
                Integer errorCount = 3;
                String url=accessTokenUrl.replace("APPID", appId).replace("SECRET", appSecret);
                accessToken="";
                while (errorCount > 0){
                    try {
                        String result= WeHttpClientUtils.get(url);
                        JSONObject jsonObject= JSON.parseObject(result);
                        accessToken=jsonObject.getString("access_token");
                        log.info("getAccessToken-result:{}",accessToken);
                        break;
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    errorCount=errorCount-1;
                }
            }

            if (StringUtils.isNotEmpty(accessToken)){
                accountDO.setToken(accessToken);
                accountDO.setExpireDate(LocalDateTime.now().plusMinutes(30));
                mpAccountService.updateAccount(MpAccountConvert.INSTANCE.convertUpdateData(accountDO));
            }
            return accessToken;
        }
        return null;
    }

    @Override
    public String createShortLink(String pageUrl, String pageTitle, Boolean isPermanent) {
        String shortLink = "";
        String accessToken = getAccessToken(false);
        if (StringUtils.isNotEmpty(accessToken)){
            Integer errorCount = 0;
            while (errorCount<3){
                String url=shortLinkUrl.replace("ACCESS_TOKEN", accessToken);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("page_url",pageUrl);
                jsonObject.put("page_title",pageTitle);
                jsonObject.put("is_permanent",isPermanent);
                HttpRequest request = new HttpRequest(url);
                String result = request.postData("", jsonObject.toJSONString());
                log.info("生成短链接结果：{}", result);
                if (StringUtils.isNotEmpty(result)){
                    JSONObject resObj = JSON.parseObject(result);
                    if (resObj.containsKey("link")){
                        shortLink = resObj.getString("link");
                    }
                }

                if (StringUtils.isNotEmpty(shortLink)){
                    break;
                }else{
                    accessToken = getAccessToken(true);
                }
                errorCount=errorCount+1;
            }

        }
        return shortLink;
    }

    @Override
    public String createQrCode(String pageUrl, Integer width) {
        String picUrl = "";
        String accessToken = getAccessToken(false);
        if (StringUtils.isNotEmpty(accessToken)){
            Integer errorCount = 0;
            while (errorCount<3){
                String url=qrcodeUrl.replace("ACCESS_TOKEN", accessToken);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("path",pageUrl);
                jsonObject.put("width",width);
                HttpRequest request = new HttpRequest(url);
                byte[] imageData = request.getPostData("", jsonObject.toJSONString());
                log.info("生成二维码结果：{}", imageData);
                if (imageData!=null && imageData.length>4024){
                    String fileName = new Date().getTime() +".jpg";
                    picUrl = fileApi.createFile(fileName,null,imageData);
                }

                if (StringUtils.isNotEmpty(picUrl)){
                    break;
                }else{
                    accessToken = getAccessToken(true);
                }
                errorCount=errorCount+1;
            }

        }
        return picUrl;
    }
}
