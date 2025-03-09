package cn.iocoder.yudao.module.member.api.exchangeRecord;


/**
 * 兑换记录 接口
 *
 * @author owen
 */
public interface ExchangeRecordApi {

    /**
     * 使用兑换码
     *
     * @param userId  用户编号
     * @param code   兑换码
     */
    void useExchangeRecord(Long userId, String code);

    /**
     * 撤销兑换码
     *
     * @param userId  用户编号
     * @param code   兑换码
     */
    void cancelExchangeRecord(Long userId, String code);

}
