package cn.iocoder.yudao.module.system.enums;

/**
 * System 字典类型的枚举类
 *
 * @author 商城管理系统
 */
public interface DictTypeConstants {

    String USER_TYPE = "user_type"; // 用户类型
    String COMMON_STATUS = "common_status"; // 系统状态

    // ========== SYSTEM 模块 ==========

    String USER_SEX = "system_user_sex"; // 用户性别

    String OPERATE_TYPE = "system_operate_type"; // 操作类型

    String LOGIN_TYPE = "system_login_type"; // 登录日志的类型
    String LOGIN_RESULT = "system_login_result"; // 登录结果

    String ERROR_CODE_TYPE = "system_error_code_type"; // 错误码的类型枚举

    String SMS_CHANNEL_CODE = "system_sms_channel_code"; // 短信渠道编码
    String SMS_TEMPLATE_TYPE = "system_sms_template_type"; // 短信模板类型
    String SMS_SEND_STATUS = "system_sms_send_status"; // 短信发送状态
    String SMS_RECEIVE_STATUS = "system_sms_receive_status"; // 短信接收状态

    String WEIFAN_CARD_TYPE  = "weifan_card_type"; // 证件类型
    String  SYSTEM_QUESTION_TOPIC_TYPE = "system_question_topic_type";//题目类型

    String WEIFAN_USER_TYPE  = "weifan_user_type"; // 用户类型
    String WEIFAN_OPEN  = "weifan_open"; // 是否公开
    String WEIFAN_TO_EXAMINE  = "weifan_to_examine"; // 审核状态
}
