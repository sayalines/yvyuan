package cn.iocoder.yudao.module.member.service.exchangeConfigItem;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfigItem.ExchangeConfigItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 兑换配置明细 Service 接口
 *
 * @author 超级管理员
 */
public interface ExchangeConfigItemService {

    /**
     * 创建兑换配置明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExchangeConfigItem(@Valid ExchangeConfigItemSaveReqVO createReqVO);

    /**
     * 更新兑换配置明细
     *
     * @param updateReqVO 更新信息
     */
    void updateExchangeConfigItem(@Valid ExchangeConfigItemSaveReqVO updateReqVO);

    /**
     * 删除兑换配置明细
     *
     * @param id 编号
     */
    void deleteExchangeConfigItem(Long id);

    /**
     * 获得兑换配置明细
     *
     * @param id 编号
     * @return 兑换配置明细
     */
    ExchangeConfigItemDO getExchangeConfigItem(Long id);

    /**
     * 获得兑换配置明细分页
     *
     * @param pageReqVO 分页查询
     * @return 兑换配置明细分页
     */
    PageResult<ExchangeConfigItemDO> getExchangeConfigItemPage(ExchangeConfigItemPageReqVO pageReqVO);


    /**
     * 创建兑换配置明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExchangeConfigItem(ExchangeConfigItemDO createReqVO);

    /**
     * 更新兑换配置明细
     *
     * @param updateReqVO 更新信息
     */
    void updateExchangeConfigItem(ExchangeConfigItemDO updateReqVO);

    /**
     * 获得兑换配置明细
     *
     * @param code 编号
     * @return 兑换配置明细
     */
    ExchangeConfigItemDO getExchangeConfigItemByCode(String code);

}