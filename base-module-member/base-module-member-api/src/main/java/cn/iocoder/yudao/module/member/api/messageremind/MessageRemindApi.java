package cn.iocoder.yudao.module.member.api.messageremind;


import java.util.Collection;
import java.util.List;

/**
 * 会员消息提醒 API 接口
 *
 * @author 商城管理系统
 */
public interface MessageRemindApi {

    /**
     * 获得消息提醒信息
     *
     * @param id 消息提醒ID
     * @return 消息提醒
     */
    MessageRemindRespDTO getMessageRemind(Long id);

    /**
     *批量更新预约到货提醒记录
     * @param skuIds
     * @return
     */
    void updateYydhRecord(List<Long> skuIds);

    /**
     *获取订单发货提醒记录
     * @param orderId
     * @return
     */
    void updateDdfhRecord(Long orderId);

    /**
     *更新记录
     * @param dto
     */
    void update(MessageRemindRespDTO dto);
}
