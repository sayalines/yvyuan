package cn.iocoder.yudao.server.controller.member;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.service.mail.MailSendService;
import cn.iocoder.yudao.module.system.service.sms.SmsSendService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.ResponseResult;
import cn.iocoder.yudao.server.vo.ApiMemberDO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户验证对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/verify")
public class LoginController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private MailSendService mailSendService;

    @Resource
    private SmsSendService smsSendService;


    @PostMapping(value = "/login")
    public ResponseResult login(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        String username = ObjectUtil.defaultIfBlank(request.getParameter("username"), "");// 用户名
        String password = ObjectUtil.defaultIfBlank(request.getParameter("password"), "");// 密码
        String code = ObjectUtil.defaultIfBlank(request.getParameter("code"), ""); // 验证码

        if (StrUtil.isBlank(username)) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("请填写手机号/邮箱！");
            return rr;
        }

        if (StrUtil.isNotBlank(code)) {
            MemberUserDO member = memberUserService.getUserByMobile(username);
            if (ObjectUtil.isNotNull(member) && ObjectUtil.isNotNull(member.getId())) {
                return mobileAndCodeLogin(member, code);
            }

            member = memberUserService.getUserByEmail(username);
            if (ObjectUtil.isNotNull(member) && ObjectUtil.isNotNull(member.getId())) {
                return emailAndCodeLogin(member, code);
            }

        } else if (StrUtil.isNotBlank(password)) {
            MemberUserDO member = memberUserService.getUserByMobile(username);
            if (ObjectUtil.isNotNull(member) && ObjectUtil.isNotNull(member.getId())) {
                return mobileAndPasswordLogin(member, password);
            }

            member = memberUserService.getUserByEmail(username);
            if (ObjectUtil.isNotNull(member) && ObjectUtil.isNotNull(member.getId())) {
                return emailAndPasswordLogin(member, password);
            }

            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("用户名或密码错误！");
            return rr;
        }
        rr.setCode(ResponseResult.ERROR);
        rr.setMessage("请先注册账号！");
        return rr;
    }


    /**
     * 发送手机号/邮箱验证码
     */
    @GetMapping(value = "/sendCode")
    public ResponseResult sendCode(HttpServletRequest request,
                                   @RequestParam(value = "account") String account,
                                   @RequestParam(value = "type", defaultValue = "email", required = false) String type) {
        ResponseResult rr = new ResponseResult();
        String validateCode = generateValidateCode(6);
        Map<String, Object> map = new HashMap<>();
        map.put("code", validateCode);
        if (type.equals("mobile")) {
            saveMobileCode(account, validateCode);
            smsSendService.sendSingleSmsToMember(account, null, "smsCode_send", map);
        } else {
            saveEmailCode(account, validateCode);
            mailSendService.sendSingleMailToMember(account, null, "emailCode_send", map);
        }
        rr.setMessage("发送成功！");
        return rr;
    }


    /**
     * 退出登录
     */
    @GetMapping(value = "/logout")
    public ResponseResult logout(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        userLogout(request);
        rr.setMessage("退出成功！");
        return rr;
    }


    private ResponseResult mobileAndCodeLogin(MemberUserDO member, String code) {
        ResponseResult rr = new ResponseResult();
        String mobile = member.getMobile();
        String redisCode = getUserMobileCode(mobile, code);
        if (StrUtil.isNotBlank(redisCode) && StrUtil.isNotBlank(code) && StrUtil.equals(redisCode, code)) {
            ApiMemberDO vo = BeanUtils.toBean(member, ApiMemberDO.class);
            String token = createToken(member.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("member", vo);
            map.put("token", token);
            rr.setData(map);
            logger.info("用户手机号【{}】+验证码登陆成功！", mobile);
        } else {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("验证码已过期");
        }
        return rr;
    }

    private ResponseResult emailAndCodeLogin(MemberUserDO member, String code) {
        ResponseResult rr = new ResponseResult();

        String email = member.getEmail();
        String redisCode = getUserEmailCode(email, code);

        if (StrUtil.isNotBlank(redisCode) && StrUtil.isNotBlank(code) && StrUtil.equals(redisCode, code)) {
            ApiMemberDO vo = BeanUtils.toBean(member, ApiMemberDO.class);
            String token = createToken(member.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("member", vo);
            map.put("token", token);
            rr.setData(map);
            logger.info("用户邮箱【{}】+验证码登陆成功！", email);
        } else {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("验证码已过期");
        }
        return rr;
    }

    private ResponseResult mobileAndPasswordLogin(MemberUserDO member, String password) {
        ResponseResult rr = new ResponseResult();
        if (ObjectUtil.isNull(member) || ObjectUtil.isNull(member.getId()) || !memberUserService.isPasswordMatch(password, member.getPassword())) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("用户名或密码错误！");
            return rr;
        }

        ApiMemberDO vo = BeanUtils.toBean(member, ApiMemberDO.class);
        String token = createToken(member.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("member", vo);
        map.put("token", token);
        rr.setData(map);
        logger.info("用户手机号【{}】+密码登陆成功！", member.getMobile());
        return rr;
    }

    private ResponseResult emailAndPasswordLogin(MemberUserDO member, String password) {
        ResponseResult rr = new ResponseResult();
        if (ObjectUtil.isNull(member) || ObjectUtil.isNull(member.getId()) || !memberUserService.isPasswordMatch(password, member.getPassword())) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("用户名或密码错误！");
            return rr;
        }

        ApiMemberDO vo = BeanUtils.toBean(member, ApiMemberDO.class);
        String token = createToken(member.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("member", vo);
        map.put("token", token);
        rr.setData(map);
        logger.info("用户邮箱【{}】+密码登陆成功！", member.getEmail());
        return rr;
    }
}
