package cn.iocoder.yudao.module.member.api.messageremind;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理后台 - 会员消息提醒 Response DTO
 *
 * @author 商城管理系统
 */
@Data
public class MessageRemindRespDTO {

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * openID
     */
    private String openid;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 业务ID
     */
    private Long bizId;
    /**
     * 是否已提醒
     */
    private Boolean isRemind;
    /**
     * 提醒时间
     */
    private LocalDateTime remindTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String creator;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     *
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String updater;
    /**
     * 是否删除
     */
    private Boolean deleted;

}
