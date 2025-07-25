package cn.iocoder.yudao.module.infra.websocket.message;

import lombok.Data;

/**
 * 示例：server -> client 同步消息
 *
 * @author 商城管理系统
 */
@Data
public class DemoReceiveMessage {

    /**
     * 接收人的编号
     */
    private Long fromUserId;
    /**
     * 内容
     */
    private String text;

    /**
     * 是否单聊
     */
    private Boolean single;

}
