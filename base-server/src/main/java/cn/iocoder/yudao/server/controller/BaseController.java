package cn.iocoder.yudao.server.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.MemberUserInfoVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.mp.controller.admin.account.vo.MpAccountRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.account.vo.MpAccountUpdateReqVO;
import cn.iocoder.yudao.module.mp.convert.account.MpAccountConvert;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.service.account.MpAccountService;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialClientDO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserBindDO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialUserBindMapper;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import cn.iocoder.yudao.module.system.service.member.MemberService;
import cn.iocoder.yudao.module.system.service.social.SocialUserService;
import cn.iocoder.yudao.server.service.RedisService;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.util.WeAPPUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;

public class BaseController {
    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
    public static final String LOGIN_TOKEN_KEY = "Authorization";
    public static final String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN_";
    public static final String PREFIX_USER_ID_TOKEN = "PREFIX_USER_ID_TOKEN_";
    public static final String PREFIX_MOBILE_CODE = "PREFIX_MOBILE_CODE_";
    public static final String PREFIX_EMAIL_CODE = "PREFIX_EMAIL_CODE_";
    private static final String REDIS_KEY_LOG_LIST = "REDIS_KEY_LOG_LIST";
    public static final String PLAT_TRACE_ID = "PLAT_TRACE_ID";
    public static String WX_ACCOUNT_APP_ID = "";
    public static String WX_ACCOUNT_APP_SECRECT = "";
    //社会化用户前缀
    public static final String SOCIAL_USER_PREFIX = "SOCIAL_";
    private static String EZMALL_WEIXIN_MA_SERVICE = "ezmall_weixin_ma_service";
    private static Map<String, WxMaService> maServices = Maps.newHashMap();
    //redis过期时间8小时
    public static final long REDIS_EXPIRE_TIME = 8 * 60 * 60;
    //redis过期时间30天
    public static final long REDIS_USER_EXPIRE_TIME = 30 * 24 * 60 * 60;
    //固定分类
    //特殊商品分类
    public static final Long CATEGORY_SPEC_ID = 10L;
    @Resource
    private RedisService redisService;
    @Resource
    private SocialUserService socialUserService;
    @Resource
    private SocialUserBindMapper socialUserBindMapper;
    @Resource
    private MemberUserService memberUserService;
    @Resource
    private MpAccountService mpAccountService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取小程序账号
     *
     * @return
     */
    public MpAccountDO getMpAccount() {
        MpAccountDO accountDO = mpAccountService.getDefaultAccount();
        if (accountDO != null) {
            WX_ACCOUNT_APP_ID = accountDO.getAppId();
            WX_ACCOUNT_APP_SECRECT = accountDO.getAppSecret();
            return accountDO;
        }
        return null;
    }

    /**
     * 小程序获取token
     *
     * @return
     */
    public String getAccessToken() {
        return getAccessToken(null);
    }

    /**
     * 小程序获取token
     *
     * @return
     */
    public String getAccessToken(MpAccountDO accountDO) {
        String accessToken = "";
        if (accountDO == null) {
            accountDO = getMpAccount();
            if (accountDO == null) {
                logger.info("获取小程序账号失败！");
                return null;
            }
        }
        accessToken = accountDO.getToken();
        LocalDateTime endTokenTime = accountDO.getExpireDate();
        if (StringUtils.isBlank(accessToken) || endTokenTime.compareTo(LocalDateTime.now()) <= 0) {
            accessToken = WeAPPUtils.getAccessToken(accountDO.getAppId(), accountDO.getAppSecret());
            if (StringUtils.isBlank(accessToken)) {
                return "";
            }

            accountDO.setToken(accessToken);
            accountDO.setExpireDate(LocalDateTime.now().plusMinutes(30));
            mpAccountService.updateAccount(MpAccountConvert.INSTANCE.convertUpdateData(accountDO));
        }
        return accessToken;
    }

    public WxMaService getwxMaService(String accessToken) {
        String serviceId = "ezmall_weixin_ma_service_" + WX_ACCOUNT_APP_ID;
        WxMaService wxMaService = maServices.get(serviceId);
        //增加一个redis标识
        if (wxMaService == null || redisService.get(serviceId) == null) {
            wxMaService = new WxMaServiceImpl();
            WxMaDefaultConfigImpl configStorage = new WxMaDefaultConfigImpl();
            configStorage.setAppid(WX_ACCOUNT_APP_ID);
            configStorage.setSecret(WX_ACCOUNT_APP_SECRECT);
            configStorage.setAccessToken(accessToken);
            wxMaService.setWxMaConfig(configStorage);
            maServices.put(serviceId, wxMaService);
        }
        return wxMaService;
    }


//    /**
//     * 获取用户openid
//     * @param code
//     * @return
//     */
//    public String getCurrentOpenId(String code){
//        MpAccountDO mpAccountDO = getMpAccount();
//        if (mpAccountDO==null){
//            logger.info("获取小程序账号失败！");
//            return null;
//        }
//        String accessToken = getAccessToken(mpAccountDO);
//        if (StringUtils.isEmpty(accessToken)){
//            logger.info("获取小程序token失败！");
//            return null;
//        }
//
//        String openid = WeAPPUtils.getOpenId(mpAccountDO.getAppId(),mpAccountDO.getAppSecret(),code);
//        if (StringUtils.isEmpty(openid)){
//            logger.info("获取小程序openid失败！");
//            return null;
//        }
//
//        return openid;
//    }

    /**
     * 根据openid查询用户信息
     *
     * @param openid
     * @return
     */
    public SocialUserDO getSocialUserByOpenId(String openid, String unionid, Integer type) {
        SocialUserDO socialUserDO = socialUserService.selectByTypeAndOpenid(SocialTypeEnum.WECHAT_MINI_APP.getType(), openid);
        if (socialUserDO == null) {
            // 获取用户IP
            String userIp = getClientIP();
            // 获得注册用户
            MemberUserDO user = memberUserService.autoCreateUser(userIp, type);
            if (user != null) {
                socialUserDO = socialUserService.createSocialUser(user.getId(), UserTypeEnum.MEMBER.getValue(), SocialTypeEnum.WECHAT_MINI_APP.getType(), openid, unionid);
            }
        } else {
            if (StringUtils.isEmpty(socialUserDO.getUnionid()) && StringUtils.isNotEmpty(unionid)) {
                socialUserDO.setUnionid(unionid);
                socialUserService.update(socialUserDO);
            }
        }
        return socialUserDO;
    }

    /**
     * 根据openid查询用户信息
     *
     * @param openid
     * @return
     */
    public SocialUserDO getSocialUserByOpenId(String openid) {
        return socialUserService.selectByTypeAndOpenid(SocialTypeEnum.WECHAT_MINI_APP.getType(), openid);
    }

    /**
     * 生成redis token
     */
    public String createToken(String socialUserId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String encryptStr = CommonUtils.encrypt(token + ":" + socialUserId);
        redisService.set(PREFIX_USER_TOKEN + encryptStr, socialUserId, REDIS_USER_EXPIRE_TIME);
        return encryptStr;
    }

    /**
     * 生成redis token
     */
    public void saveUserId(Long userId, String token) {
        if (userId != null && StringUtils.isNotEmpty(token)) {
            Object obj = redisService.get(PREFIX_USER_ID_TOKEN + token);
            if (obj == null) {
                redisService.set(PREFIX_USER_ID_TOKEN + token, userId, REDIS_USER_EXPIRE_TIME);
            }
        }
    }

    public String createToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        redisService.set(PREFIX_USER_TOKEN + token, userId, REDIS_EXPIRE_TIME);
        return token;
    }


    /**
     * 获取当前社会化用户
     *
     * @param rq
     * @return
     */
    public SocialUserDO getCurrentSocialUser(HttpServletRequest rq) {
        String token = rq.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            String socialUserId = getSocialUserId(token);
            if (CommonUtils.isLong(socialUserId)) {
                SocialUserDO socialUserDO = socialUserService.getSocialUser(Long.valueOf(socialUserId));
                if (socialUserDO != null) {
                    return socialUserDO;
                }
            }
        }
        return null;
    }

    /**
     * 获取当前用户OpenId
     *
     * @param rq
     * @return
     */
    public String getUserOpenId(HttpServletRequest rq) {
        String token = rq.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            String socialUserId = getSocialUserId(token);
            if (CommonUtils.isLong(socialUserId)) {
                SocialUserDO socialUserDO = socialUserService.getSocialUser(Long.valueOf(socialUserId));
                if (socialUserDO != null) {
                    return socialUserDO.getOpenid();
                }
            }
        }
        return null;
    }

    /**
     * 保存redis值
     *
     * @param key
     * @param value
     */
    public void saveRedisValue(String key, String value) {
        try {
            redisService.set(key, value, REDIS_EXPIRE_TIME);
        } catch (Exception e) {
            logger.error("saveRedisValue error :{}", e.getMessage());
        }
    }

    /**
     * 保存redis值
     *
     * @param key
     * @param value
     */
    public void saveRedisValue(String key, String value,long time) {
        try {
            redisService.set(key, value, time);
        } catch (Exception e) {
            logger.error("saveRedisValue error :{}", e.getMessage());
        }
    }

    /**
     * 读取redis值
     *
     * @param key
     * @return
     */
    public String getRedisValue(String key) {
        String result = "";
        try {
            Object obj = redisService.get(key);
            if (obj != null) {
                result = String.valueOf(obj);
            }
        } catch (Exception e) {
            logger.error("getRedisValue error :{}", e.getMessage());
        }
        return result;
    }

    /**
     * 获取会员信息
     *
     * @param socialUserId
     * @return
     */
    public MemberUserDO getMemberUser(Long socialUserId) {
        if (socialUserId != null) {
            SocialUserBindDO socialUserBind = socialUserBindMapper.selectByUserTypeAndSocialUserId(UserTypeEnum.MEMBER.getValue(), socialUserId);
            if (socialUserBind != null && socialUserBind.getUserId() != null) {
                MemberUserDO memberUserDO = memberUserService.getUser(socialUserBind.getUserId());
                if (memberUserDO != null) {
                    return memberUserDO;
                }
            }
        }
        return null;
    }

    public Long getSocialUserId(Long userId) {
        List<SocialUserBindDO> list = socialUserBindMapper.selectListByUserIdAndUserType(userId,UserTypeEnum.MEMBER.getValue());
        if (list!=null && list.size()>0){
            return list.get(0).getSocialUserId();
        }
        return 0L;
    }


    /**
     * 获取当前用户ID
     *
     * @param rq
     * @return
     */
    public Long getCurrentUserId(HttpServletRequest rq) {
        Long result = null;
        String token = rq.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            String socialUserId = getSocialUserId(token);
            if (CommonUtils.isLong(socialUserId)) {
                SocialUserBindDO socialUserBind = socialUserBindMapper.selectByUserTypeAndSocialUserId(UserTypeEnum.MEMBER.getValue(), Long.valueOf(socialUserId));
                if (socialUserBind != null && socialUserBind.getUserId() != null) {
                    result = socialUserBind.getUserId();
                }
            }
        } else {
            //获取测试账号ID
            String tsCustomerId = rq.getHeader("tsCustomerId");
            if (StringUtils.isNotEmpty(tsCustomerId)) {
                result = Long.valueOf(tsCustomerId);
            }
        }
        return result;
    }

    /**
     * 获取当前用户
     *
     * @param rq
     * @return
     */
    public MemberUserDO getCurrentUser(HttpServletRequest rq) {
        String token = rq.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            String socialUserId = getSocialUserId(token);
            if (CommonUtils.isLong(socialUserId)) {
                MemberUserDO memberUserDO = memberUserService.getMemberUserBySocialUserId(Long.valueOf(socialUserId));
                if (memberUserDO != null) {
                    saveUserId(memberUserDO.getId(), token);
                    return memberUserDO;
                }
            }
        } else {
            //获取测试账号ID
            String tsCustomerId = rq.getHeader("tsCustomerId");
            if (StringUtils.isNotEmpty(tsCustomerId)) {
                MemberUserDO memberUserDO = memberUserService.getUser(Long.valueOf(tsCustomerId));
                if (memberUserDO != null) {
                    saveUserId(memberUserDO.getId(), "CS_" + tsCustomerId);
                    return memberUserDO;
                }
            }
        }
        return null;
    }

    public String getSocialUserId(String token) {
        String socialUserId = "";
        Object obj = redisService.get(PREFIX_USER_TOKEN + token);
        if (obj != null) {
            socialUserId = String.valueOf(obj);
        } else {
            //重新刷新token
            try {
                String decryptStr = CommonUtils.decrypt(token);
                if (StringUtils.isNotEmpty(decryptStr)) {
                    socialUserId = decryptStr.split(":")[1];
                    if (CommonUtils.isLong(socialUserId)) {
                        MemberUserInfoVO userInfo = memberUserService.getUserInfoBySocialUserId(Long.valueOf(socialUserId));
                        if (userInfo != null) {
                            redisService.set(PREFIX_USER_TOKEN + token, socialUserId, REDIS_USER_EXPIRE_TIME);
                            saveUserId(userInfo.getId(), token);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return socialUserId;
    }

    public Long getUserId(HttpServletRequest request) {
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            Object obj = redisService.get(PREFIX_USER_ID_TOKEN + token);
            if (obj != null) {
                return Long.valueOf(String.valueOf(obj));
            }
        } else {
            String tsCustomerId = request.getHeader("tsCustomerId");
            if (StringUtils.isNotEmpty(tsCustomerId)) {
                return Long.valueOf(tsCustomerId);
            }
        }
        return null;
    }

    public void writeLog(String content) {
        redisTemplate.opsForList().leftPush(REDIS_KEY_LOG_LIST, content);
    }

    /**
     * 跟踪编号
     *
     * @param request
     * @return
     */
    public String getTradeId(HttpServletRequest request) {
        if (request.getAttribute(PLAT_TRACE_ID) != null) {
            return (String) request.getAttribute(PLAT_TRACE_ID);
        }
        return null;
    }

    /**
     * 获取手机号短信验证码
     */
    protected String getUserMobileCode(String mobile, String code) {
        if (redisService.hasKey(PREFIX_MOBILE_CODE + mobile + "_" + code)) {
            return StrUtil.toString(redisService.get(PREFIX_MOBILE_CODE + mobile + "_" + code));
        }
        return null;
    }

    /**
     * 获取邮箱验证码
     */
    protected String getUserEmailCode(String email, String code) {
        if (redisService.hasKey(PREFIX_EMAIL_CODE + email + "_" + code)) {
            return StrUtil.toString(redisService.get(PREFIX_EMAIL_CODE + email + "_" + code));
        }
        return null;
    }

    /**
     * 退出登陆
     */
    protected void userLogout(HttpServletRequest request) {
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (StrUtil.isNotBlank(token)) {
            if (redisService.hasKey(PREFIX_USER_TOKEN + token)) {
                redisService.del(PREFIX_USER_TOKEN + token);
            }
        }
    }

    /**
     * 生成验证码
     */
    protected String generateValidateCode(int limit) {
        int min = (int) Math.pow(10, limit - 1);
        int max = (int) Math.pow(10, limit) - 1;
        return StrUtil.toString(RandomUtil.randomInt(min, max + 1));
    }


    /**
     * 保存手机号验证码
     */
    protected void saveMobileCode(String mobile, String code) {
        redisService.set(PREFIX_MOBILE_CODE + mobile + "_" + code, code, 60 * 30);
    }

    /**
     * 保存邮箱验证码
     */
    protected void saveEmailCode(String email, String code) {
        redisService.set(PREFIX_EMAIL_CODE + email + "_" + code, code, 60 * 30);
    }
}
