package cn.iocoder.yudao.framework.sms.core.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 短信框架的错误码枚举
 *
 * 短信框架，使用 2-001-000-000 段
 *
 * @author 商城管理系统
 */
public interface SmsFrameworkErrorCodeConstants {

    ErrorCode SMS_UNKNOWN = new ErrorCode(2_001_000_000, "未知错误，需要解析");

    // ========== 权限 / 限流等相关 2-001-000-100 ==========

    ErrorCode SMS_PERMISSION_DENY = new ErrorCode(2_001_000_100, "没有发送短信的权限");
    ErrorCode SMS_IP_DENY = new ErrorCode(2_001_000_100, "IP 不允许发送短信");

    // 阿里云：将短信发送频率限制在正常的业务限流范围内。默认短信验证码：使用同一签名，对同一个手机号验证码，支持 1 条 / 分钟，5 条 / 小时，累计 10 条 / 天。
    ErrorCode SMS_SEND_BUSINESS_LIMIT_CONTROL = new ErrorCode(2_001_000_102, "指定手机的发送限流");
    // 阿里云：已经达到您在控制台设置的短信日发送量限额值。在国内消息设置 > 安全设置，修改发送总量阈值。
    ErrorCode SMS_SEND_DAY_LIMIT_CONTROL = new ErrorCode(2_001_000_103, "每天的发送限流");

    ErrorCode SMS_SEND_CONTENT_INVALID = new ErrorCode(2_001_000_104, "短信内容有敏感词");

    // 腾讯云：为避免骚扰用户，营销短信只允许在8点到22点发送。
    ErrorCode SMS_SEND_MARKET_LIMIT_CONTROL = new ErrorCode(2_001_000_105, "营销短信发送时间限制");

    // ========== 模板相关 2-001-000-200 ==========
    ErrorCode SMS_TEMPLATE_INVALID = new ErrorCode(2_001_000_200, "短信模板不合法"); // 包括短信模板不存在
    ErrorCode SMS_TEMPLATE_PARAM_ERROR = new ErrorCode(2_001_000_201, "模板参数不正确");

    // ========== 签名相关 2-001-000-300 ==========
    ErrorCode SMS_SIGN_INVALID = new ErrorCode(2_001_000_300, "短信签名不可用");

    // ========== 账户相关 2-001-000-400 ==========
    ErrorCode SMS_ACCOUNT_MONEY_NOT_ENOUGH = new ErrorCode(2_001_000_400, "账户余额不足");
    ErrorCode SMS_ACCOUNT_INVALID = new ErrorCode(2_001_000_401, "apiKey 不存在");

    // ========== 其它相关 2-001-000-900 开头 ==========
    ErrorCode SMS_API_PARAM_ERROR = new ErrorCode(2_001_000_900, "请求参数缺失");
    ErrorCode SMS_MOBILE_INVALID = new ErrorCode(2_001_000_901, "手机格式不正确");
    ErrorCode SMS_MOBILE_BLACK = new ErrorCode(2_001_000_902, "手机号在黑名单中");
    ErrorCode SMS_APP_ID_INVALID = new ErrorCode(2_001_000_903, "SdkAppId不合法");

    ErrorCode EXCEPTION = new ErrorCode(2_001_000_999, "调用异常");

}
