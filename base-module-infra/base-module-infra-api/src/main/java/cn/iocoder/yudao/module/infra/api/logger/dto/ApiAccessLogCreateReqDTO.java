package cn.iocoder.yudao.module.infra.api.logger.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * API 访问日志
 *
 * @author 商城管理系统
 */
@Data
public class ApiAccessLogCreateReqDTO {

    /**
     * 链路追踪编号
     */
    private String traceId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 请求方法名
     */
    private String requestMethod;
    /**
     * 访问地址
     */
    private String requestUrl;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 浏览器 UA
     */
    private String userAgent;

    /**
     * 开始请求时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束请求时间
     */
    private LocalDateTime endTime;
    /**
     * 执行时长，单位：毫秒
     */
    private Integer duration;
    /**
     * 结果码
     */
    private Integer resultCode;
    /**
     * 结果提示
     */
    private String resultMsg;

    /**
     * 类型   0：API   1：VISIT
     */
    private Integer type;
    /**
     * 标题
     */
    private String title;
    /**
     * 手机型号
     */
    private String mobileModel;
    /**
     * 分组
     */
    private String groupName;
    /**
     * 业务ID
     */
    private String bizId;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 浏览器信息
     * */
    private String browser;
    /**
     * 来源
     * */
    private String origins;

}
