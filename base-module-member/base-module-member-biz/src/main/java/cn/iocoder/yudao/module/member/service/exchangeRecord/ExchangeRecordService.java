package cn.iocoder.yudao.module.member.service.exchangeRecord;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.exchangeRecord.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeRecord.ExchangeRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 会员兑换记录 Service 接口
 *
 * @author 超级管理员
 */
public interface ExchangeRecordService {

    /**
     * 创建会员兑换记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExchangeRecord(@Valid ExchangeRecordSaveReqVO createReqVO);

    /**
     * 更新会员兑换记录
     *
     * @param updateReqVO 更新信息
     */
    void updateExchangeRecord(@Valid ExchangeRecordSaveReqVO updateReqVO);

    /**
     * 删除会员兑换记录
     *
     * @param id 编号
     */
    void deleteExchangeRecord(Long id);

    /**
     * 获得会员兑换记录
     *
     * @param id 编号
     * @return 会员兑换记录
     */
    ExchangeRecordDO getExchangeRecord(Long id);

    /**
     * 获得会员兑换记录分页
     *
     * @param pageReqVO 分页查询
     * @return 会员兑换记录分页
     */
    PageResult<ExchangeRecordRespVO> getExchangeRecordPage(ExchangeRecordPageReqVO pageReqVO);

    /**
     *查询兑换码使用记录
      * @param code
     * @return
     */
    Integer getExchangeRecordListByCode(String code);

    /**
     * 使用兑换码
     * @param userId
     * @param code
     */
    ExchangeRecordDO takeCode(Long userId,String code);
    /**
     * 获取用户可用兑换记录
     *
     * @param userId 用户编号
     * @return 会员兑换记录
     */
    ExchangeRecordDO getUserCanUseExchangeRecord(Long userId,String code);
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

    /**
     * 获取今天用户兑换记录数
     * @param userId
     * @return
     */
    Long getUserExchangeRecordCountByToday(Long userId);
    /**
     * 获取今天用户兑换记录数
     * @param userId
     * @param configId
     * @return
     */
    Long getUserExchangeRecordCount(Long userId,Long configId);
}