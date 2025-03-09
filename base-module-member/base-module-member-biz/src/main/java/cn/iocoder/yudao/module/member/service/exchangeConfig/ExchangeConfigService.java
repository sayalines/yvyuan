package cn.iocoder.yudao.module.member.service.exchangeConfig;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.exchangeConfig.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfig.ExchangeConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 兑换配置 Service 接口
 *
 * @author 超级管理员
 */
public interface ExchangeConfigService {

    /**
     * 创建兑换配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExchangeConfig(@Valid ExchangeConfigSaveReqVO createReqVO);

    /**
     * 更新兑换配置
     *
     * @param updateReqVO 更新信息
     */
    void updateExchangeConfig(@Valid ExchangeConfigSaveReqVO updateReqVO);

    /**
     * 删除兑换配置
     *
     * @param id 编号
     */
    void deleteExchangeConfig(Long id);

    /**
     * 获得兑换配置
     *
     * @param id 编号
     * @return 兑换配置
     */
    ExchangeConfigDO getExchangeConfig(Long id);

    /**
     * 获得兑换配置分页
     *
     * @param pageReqVO 分页查询
     * @return 兑换配置分页
     */
    PageResult<ExchangeConfigDO> getExchangeConfigPage(ExchangeConfigPageReqVO pageReqVO);

    /**
     * 生成兑换码
     * @param configId
     * @param count
     */
    void createExchange(Long configId,Integer count);

}