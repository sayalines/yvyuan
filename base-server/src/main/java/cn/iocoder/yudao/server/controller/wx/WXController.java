package cn.iocoder.yudao.server.controller.wx;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.system.enums.common.SexEnum;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderQueryService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.util.ResponseResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "微信对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/wx")
public class WXController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(WXController.class);

    @Resource
    private TradeOrderQueryService tradeOrderQueryService;

    @Resource
    private MemberLevelService memberLevelService;
    @Resource
    private MemberUserService memberUserService;
    /**
     * 微信用户登录
     * @param code
     * @param ts
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseResult weixinAppLogin(HttpServletRequest request,String code, String ts, Integer type) {
        if (StringUtils.isBlank(code)) {
            return new ResponseResult(ResponseResult.ERROR, "code不能为空", null);
        }
        ResponseResult rr = new ResponseResult();
        try {
            logger.info("小程序登录 code:{}", code);
            String openId = null;
            String unionId = null;
            String sessionKey="";
            //客户账套调试
            if (StringUtils.isNotBlank(ts) && ts.equalsIgnoreCase("gzdy")){
                openId=code;
            }else{
                try {
                    String accessToken = getAccessToken();
                    logger.info("accessToken:{}", accessToken);
                    if (StringUtils.isBlank(accessToken)){
                        logger.error("accessToken不能为空");
                        rr.setMessage("accessToken不能为空");
                        return rr;
                    }
                    WxMaJscode2SessionResult result = getwxMaService(accessToken).getUserService().getSessionInfo(code);
                    logger.info("微信登录获取openid_session-key openId:" + result.getOpenid()+",unionId:"+result.getUnionid()+" ~~~ "+result.getSessionKey());
                    openId = result.getOpenid();//openid
                    unionId = result.getUnionid();//unionid
                    sessionKey = result.getSessionKey();
                } catch (Exception we) {
                    logger.error("get openid error:{}",we.getMessage());
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage(we.getMessage());
                    return rr;
                }
            }

            if (StringUtils.isNotBlank(openId)) {
                saveRedisValue(openId,sessionKey,-1);

                SocialUserDO socialUser = getSocialUserByOpenId(openId,unionId,type);
                if (socialUser==null){
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("get socialUser error");
                    return rr;
                }
                rr.setCode(ResponseResult.SUCCESS);
                rr.setMessage("用户微信登录成功");
                String token = createToken(socialUser.getId().toString());
                Map<String, Object> data = new HashMap<>();
                data.put("openid", openId);
                data.put("unionid", unionId);
                data.put("token", token);
                MemberUserDO member = getMemberUser(socialUser.getId());
                if (member!=null){
                    if (member.getStatus().equals(1)){
                        logger.info("当前用户已停用，请联系客服人员");
                        rr.setMessage("当前用户已停用，请联系客服人员");
                        rr.setCode(ResponseResult.ERROR);
                        return rr;
                    }

                    String levelName="";
                    if (member.getLevelId()!=null){
                        MemberLevelDO memberLevelDO = memberLevelService.getLevel(member.getLevelId());
                        if (memberLevelDO!=null){
                            levelName = memberLevelDO.getName();
                        }
                    }
                    Map<String,Object> map = new HashMap<>();
                    map.put("user_id",member.getId());
                    map.put("user_name",member.getNickname());
                    map.put("user_avatar",member.getAvatar());
                    map.put("user_phone",member.getMobile());
                    map.put("user_sex", CommonUtils.formatSex(member.getSex()));
                    map.put("level_id",member.getLevelId());
                    map.put("level_name",levelName);
                    data.put("user",map);
                    //更新登录信息
                    member.setLoginDate(LocalDateTime.now());
                    member.setLoginIp(ServletUtils.getClientIP(request));
                    memberUserService.updateUser(member);

                    saveUserId(member.getId(),token);
                }else{
                    logger.info("会员信息找不到");
                    rr.setMessage("会员信息找不到");
                    rr.setCode(ResponseResult.ERROR);
                    return rr;
                }
                rr.setData(data);
                logger.info("用户微信登录成功");
                return rr;
            } else {
                logger.info("用户openid空，微信登录失败");
                rr.setMessage("用户微信登录失败");
                rr.setCode(ResponseResult.ERROR);
                return rr;
            }
        } catch (Exception e) {
            logger.error("wx oauth login error:{}", e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("系统错误，请联系管理员");
            return rr;
        }
    }


    /**
     * 刷新用户token
     * @param openId
     * @return
     */
    @PostMapping(value = "/refresh/token")
    public ResponseResult refreshToken(String openId) {
        if (StringUtils.isBlank(openId)) {
            return new ResponseResult(ResponseResult.ERROR, "openId不能为空", null);
        }
        ResponseResult rr = new ResponseResult();
        try {
            logger.info("刷新用户token openId:{}", openId);
            if (StringUtils.isNotBlank(openId)) {
                SocialUserDO socialUser = getSocialUserByOpenId(openId);
                if (socialUser==null){
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("get socialUser error");
                    return rr;
                }
                rr.setCode(ResponseResult.SUCCESS);
                rr.setMessage("刷新token成功");
                String token = createToken(socialUser.getId().toString());
                Map<String, Object> data = new HashMap<>();
                data.put("openid", openId);
                data.put("unionid", socialUser.getUnionid());
                data.put("token", token);
                MemberUserDO member = getMemberUser(socialUser.getId());
                if (member!=null){
                    String levelName="";
                    if (member.getLevelId()!=null){
                        MemberLevelDO memberLevelDO = memberLevelService.getLevel(member.getLevelId());
                        if (memberLevelDO!=null){
                            levelName = memberLevelDO.getName();
                        }
                    }
                    Map<String,Object> map = new HashMap<>();
                    map.put("user_id",member.getId());
                    map.put("user_name",member.getNickname());
                    map.put("user_avatar",member.getAvatar());
                    map.put("user_phone",member.getMobile());
                    map.put("user_sex", CommonUtils.formatSex(member.getSex()));
                    map.put("user_isRegister",member.getIsRegister());
                    map.put("isNewUser",tradeOrderQueryService.isNewUser(member.getId()));
                    map.put("isTakeNewGiftPack",member.getIsTakeNewGiftPack());
                    map.put("level_id",member.getLevelId());
                    map.put("level_name",levelName);
                    data.put("user",map);

                    saveUserId(member.getId(),token);
                }else{
                    logger.info("会员信息找不到");
                    rr.setMessage("会员信息找不到");
                    rr.setCode(ResponseResult.ERROR);
                    return rr;
                }
                rr.setData(data);
                logger.info("用户微信登录成功");
                return rr;
            } else {
                logger.info("用户openid空，刷新token失败");
                rr.setMessage("刷新token失败");
                rr.setCode(ResponseResult.ERROR);
                return rr;
            }
        } catch (Exception e) {
            logger.error("wx refresh tokenerror:{}", e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("系统错误，请联系管理员");
            return rr;
        }
    }



//    /**
//     * (解密用户信息)解密微信加密的信息
//     * @param encryptData
//     * @param iv
//     * @return
//     */
//    @PostMapping(value = "/decryptinfo")
//    @ResponseBody
//    public ResponseResult decrypt(String encryptData, String iv, String openId,String userId, HttpServletRequest request) {
//        ResponseResult rr = new ResponseResult();
//        if (StringUtils.isBlank(openId)) {
//            return new ResponseResult(ResponseResult.ERROR, "no token", null);
//        }
//        if (StringUtils.isBlank(userId)) {
//            return new ResponseResult(ResponseResult.ERROR, "no userId", null);
//        }
//        try {
//            String session_Key = getRedisValue(openId);
//            logger.error("decrypt: wx oppenid{}",openId);
//            logger.error("decrypt: wx session_Key{}",session_Key);
//            logger.info("decrypt data:{}",session_Key + " --- " + encryptData + " --- " + iv);
//            if (StringUtils.isBlank(session_Key)) {
//                logger.error("no session key error");
//                return new ResponseResult(ResponseResult.ERROR, "no session key", null);
//            }
//            try {
//                String accessToken = getAccessToken();
//                WxMaUserInfo wmi = getwxMaService(accessToken).getUserService().getUserInfo(session_Key,encryptData,iv);
//                if (wmi==null){
//                    return new ResponseResult(ResponseResult.ERROR, "解析数据失败", null);
//                }
//
//                MemberUserDO member = memberUserService.getUser(Long.valueOf(userId));
//                if (member==null){
//                    return new ResponseResult(ResponseResult.ERROR, "找不到会员信息", null);
//                }
//                member.setNickname(wmi.getNickName());
//                member.setAvatar(wmi.getAvatarUrl());
//                if (StringUtils.isNotBlank(wmi.getGender())){
//                    if (wmi.getGender().equalsIgnoreCase("1")){
//                        member.setSex(SexEnum.MALE.getSex());
//                    }else{
//                        member.setSex(SexEnum.FEMALE.getSex());
//                    }
//                }else{
//                    member.setSex(SexEnum.UNKNOWN.getSex());
//                }
//                member.setIsRegister(true);
//                memberUserService.updateUser(member);
//
//                Map<String,Object> map = new HashMap<>();
//                map.put("user_id",member.getId());
//                map.put("user_name",member.getNickname());
//                map.put("user_avatar",member.getAvatar());
//                map.put("user_phone",member.getMobile());
//                map.put("user_isRegister",member.getIsRegister());
//                rr.setCode(ResponseResult.SUCCESS);
//                rr.setMessage("操作成功");
//                rr.setData(map);
//                return rr;
//            } catch (Exception e) {
//                logger.error(" {} decrypt data error:{}", session_Key,e);
//                rr.setCode(ResponseResult.ERROR);
//                rr.setMessage(e.getMessage());
//                return rr;
//            }
//
//        } catch (Exception e) {
//            logger.error("decrypt: wx oauth login error:{}", e);
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage("系统错误，请联系管理员");
//            return rr;
//        }
//    }

    public String getSessionKey(String code){
        try {
            String accessToken = getAccessToken();
            logger.info("accessToken:{}", accessToken);
            if (StringUtils.isBlank(accessToken)) {
                logger.error("accessToken不能为空");
                return null;
            }
            WxMaJscode2SessionResult result = getwxMaService(accessToken).getUserService().getSessionInfo(code);
            return result.getSessionKey();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  解密手机号
     * @param encryptedData 解密手机号参数
     * @param iv 机密手机号参数
     * @param openId openId
     * @return
     */
    @PostMapping(value = "/decryptphone")
    @ResponseBody
    public ResponseResult decryptPhone(HttpServletRequest request,String encryptedData, String iv,String code) {
        logger.info("/wx/decryptphone start");
        logger.info("decryptPhone: wx code: {}",code);
        logger.info("decryptPhone: wx encryptedData: {}",encryptedData);
        logger.info("decryptPhone: wx iv: {}",iv);
        ResponseResult rr = new ResponseResult();
        String session_Key = "";
        if (StringUtils.isNotEmpty(code)){
            session_Key = getSessionKey(code);
        }
        if (StringUtils.isBlank(session_Key)) {
            return new ResponseResult(ResponseResult.ERROR, "no token", null);
        }
        try {
            logger.info("decryptPhone: wx session_Key: {}",session_Key);
            logger.info("decryptPhone data:{}",session_Key + " --- " + encryptedData + " --- " + iv);
            if (StringUtils.isBlank(session_Key)) {
                userLogout(request);
                return new ResponseResult(ResponseResult.NOLOGIN, "参数已过期，请重新登录", null);
            }
            try {
                String accessToken = getAccessToken();
                WxMaPhoneNumberInfo wpn = getwxMaService(accessToken).getUserService().getPhoneNoInfo(session_Key,encryptedData,iv);
                String mobile = wpn.getPhoneNumber();
                rr.setCode(ResponseResult.SUCCESS);
                rr.setMessage("操作成功");
                rr.setData(mobile);
                return rr;
            } catch (Exception e) {
                logger.error("{} decryptPhone data error:{}", session_Key,e.getMessage());
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage(e.getMessage());
                return rr;
            }
        } catch (Exception e) {
            logger.error("decryptPhone: wx oauth login error:{}", e);
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("系统错误，请联系管理员");
            return rr;
        }
    }
}
