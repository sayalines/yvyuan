package cn.iocoder.yudao.module.member.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Member 错误码枚举类
 * <p>
 * member 系统，使用 1-004-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 用户相关  1-004-001-000 ============
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_004_001_000, "用户不存在");
    ErrorCode USER_MOBILE_NOT_EXISTS = new ErrorCode(1_004_001_001, "手机号未注册用户");
    ErrorCode USER_MOBILE_USED = new ErrorCode(1_004_001_002, "修改手机失败，该手机号({})已经被使用");
    ErrorCode USER_POINT_NOT_ENOUGH = new ErrorCode(1_004_001_003, "用户积分余额不足");

    // ========== AUTH 模块 1-004-003-000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_004_003_000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1_004_003_001, "登录失败，账号被禁用");
    ErrorCode AUTH_THIRD_LOGIN_NOT_BIND = new ErrorCode(1_004_003_005, "未绑定账号，需要进行绑定");
    ErrorCode AUTH_MOBILE_USED = new ErrorCode(1_004_003_007, "手机号已经被使用");

    // ========== 用户收件地址 1-004-004-000 ==========
    ErrorCode ADDRESS_NOT_EXISTS = new ErrorCode(1_004_004_000, "用户收件地址不存在");

    //========== 用户标签 1-004-006-000 ==========
    ErrorCode TAG_NOT_EXISTS = new ErrorCode(1_004_006_000, "用户标签不存在");
    ErrorCode TAG_NAME_EXISTS = new ErrorCode(1_004_006_001, "用户标签已经存在");
    ErrorCode TAG_HAS_USER = new ErrorCode(1_004_006_002, "用户标签下存在用户，无法删除");

    //========== 积分配置 1-004-007-000 ==========

    //========== 积分记录 1-004-008-000 ==========
    ErrorCode POINT_RECORD_BIZ_NOT_SUPPORT = new ErrorCode(1_004_008_000, "用户积分记录业务类型不支持");

    //========== 签到配置 1-004-009-000 ==========
    ErrorCode SIGN_IN_CONFIG_NOT_EXISTS = new ErrorCode(1_004_009_000, "签到天数规则不存在");
    ErrorCode SIGN_IN_CONFIG_EXISTS = new ErrorCode(1_004_009_001, "签到天数规则已存在");

    //========== 签到配置 1-004-010-000 ==========
    ErrorCode SIGN_IN_RECORD_TODAY_EXISTS = new ErrorCode(1_004_010_000, "今日已签到，请勿重复签到");

    //========== 用户等级 1-004-011-000 ==========
    ErrorCode LEVEL_NOT_EXISTS = new ErrorCode(1_004_011_000, "用户等级不存在");
    ErrorCode LEVEL_NAME_EXISTS = new ErrorCode(1_004_011_001, "用户等级名称[{}]已被使用");
    ErrorCode LEVEL_VALUE_EXISTS = new ErrorCode(1_004_011_002, "用户等级值[{}]已被[{}]使用");
    ErrorCode LEVEL_EXPERIENCE_MIN = new ErrorCode(1_004_011_003, "升级经验必须大于上一个等级[{}]设置的升级经验[{}]");
    ErrorCode LEVEL_EXPERIENCE_MAX = new ErrorCode(1_004_011_004, "升级经验必须小于下一个等级[{}]设置的升级经验[{}]");
    ErrorCode LEVEL_HAS_USER = new ErrorCode(1_004_011_005, "用户等级下存在用户，无法删除");

    ErrorCode EXPERIENCE_BIZ_NOT_SUPPORT = new ErrorCode(1_004_011_201, "用户经验业务类型不支持");

    //========== 用户分组 1-004-012-000 ==========
    ErrorCode GROUP_NOT_EXISTS = new ErrorCode(1_004_012_000, "用户分组不存在");
    ErrorCode GROUP_HAS_USER = new ErrorCode(1_004_012_001, "用户分组下存在用户，无法删除");

    ErrorCode CARD_EXCHANGE_CONFIG_ERROR = new ErrorCode(1_006_001_006, "后台数据配置错误");
    // ========== 任务配置 TODO 补充编号 ==========
    ErrorCode TASK_CONFIG_NOT_EXISTS = new ErrorCode(1_010_001_001, "任务配置不存在");
    // ========== 兑换配置 TODO 补充编号 ==========
    ErrorCode EXCHANGE_CONFIG_NOT_EXISTS = new ErrorCode(1_011_001_001, "兑换配置不存在");
    ErrorCode EXCHANGE_CONFIG_NOT_PARAM = new ErrorCode(1_011_001_002, "缺少参数");
    // ========== 兑换配置明细 TODO 补充编号 ==========
    ErrorCode EXCHANGE_CONFIG_ITEM_NOT_EXISTS = new ErrorCode(1_011_001_002, "兑换配置明细不存在");
    // ========== 会员兑换记录 TODO 补充编号 ==========
    ErrorCode EXCHANGE_RECORD_NOT_EXISTS = new ErrorCode(1_011_001_002, "会员兑换记录不存在");

    ErrorCode EXCHANGE_RECORD_NOT_PARAM = new ErrorCode(1_011_001_003, "缺少参数");

    ErrorCode EXCHANGE_RECORD_ERROR_1 = new ErrorCode(1_011_001_004, "您输入的兑换码有误，请重新核对后再输入");
    ErrorCode EXCHANGE_RECORD_ERROR_2 = new ErrorCode(1_011_001_005, "此活动还未开始，如果疑问请联系小程序客服");
    ErrorCode EXCHANGE_RECORD_ERROR_3 = new ErrorCode(1_011_001_006, "此活动已结束，如果疑问请联系小程序客服");
    ErrorCode EXCHANGE_RECORD_ERROR_4 = new ErrorCode(1_011_001_007, "此兑换码已兑换，如果疑问请联系小程序客服");
    ErrorCode EXCHANGE_RECORD_ERROR_5 = new ErrorCode(1_011_001_008, "此兑换码已被领取完，如果疑问请联系小程序客服");
    ErrorCode EXCHANGE_RECORD_ERROR_6 = new ErrorCode(1_011_001_009, "没有找到兑换码使用记录");
    ErrorCode EXCHANGE_RECORD_ERROR_7 = new ErrorCode(1_011_001_009, "本日兑换次数已达上限，如果疑问请联系小程序客服");
    ErrorCode EXCHANGE_RECORD_ERROR_9 = new ErrorCode(1_011_001_009, "兑换次数已达上限，如果疑问请联系小程序客服");

    // ========== 会员消息提醒 TODO 补充编号 ==========
    ErrorCode MESSAGE_REMIND_NOT_EXISTS = new ErrorCode(1_012_001_001, "会员消息提醒不存在");
    // ========== 访客信息日志 TODO 补充编号 ==========
    ErrorCode VISIT_LOG_NOT_EXISTS = new ErrorCode(1_018_001_001, "访客信息日志不存在");
    // ========== 访客行为日志 TODO 补充编号 ==========
    ErrorCode ACTION_LOG_NOT_EXISTS = new ErrorCode(1_018_001_002, "访客行为日志不存在");
    // ========== 抽盒访问日志 TODO 补充编号 ==========
    ErrorCode PRODUCT_VISIT_LOG_NOT_EXISTS = new ErrorCode(1_018_001_004, "商品访问日志不存在");
    // ========== 预约记录 TODO 补充编号 ==========
    ErrorCode APPOINT_RECORD_NOT_EXISTS = new ErrorCode(1_019_001_001, "预约记录不存在");
    // ========== 车辆登记 TODO 补充编号 ==========
    ErrorCode VEHICLE_RECORD_NOT_EXISTS = new ErrorCode(1_020_001_001, "车辆登记不存在");
    // ========== 问卷记录 TODO 补充编号 ==========
    ErrorCode QUESTION_RECORD_NOT_EXISTS = new ErrorCode(1_021_001_001, "问卷记录不存在");

    // ========== 问题反馈 TODO 补充编号 ==========
    ErrorCode QUESTION_NOT_EXISTS = new ErrorCode(1_029_001_001, "问题反馈不存在");
    // ========== 文章访问日志 TODO 补充编号 ==========
    ErrorCode ARTICLE_VISIT_NOT_EXISTS = new ErrorCode(1_030_001_001, "文章访问日志不存在");
}
