package cn.iocoder.yudao.module.member.service.exchangeConfigItem;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfigItem.ExchangeConfigItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.exchangeConfigItem.ExchangeConfigItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 兑换配置明细 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class ExchangeConfigItemServiceImpl implements ExchangeConfigItemService {

    @Resource
    private ExchangeConfigItemMapper exchangeConfigItemMapper;

    @Override
    public Long createExchangeConfigItem(ExchangeConfigItemSaveReqVO createReqVO) {
        // 插入
        ExchangeConfigItemDO exchangeConfigItem = BeanUtils.toBean(createReqVO, ExchangeConfigItemDO.class);
        exchangeConfigItemMapper.insert(exchangeConfigItem);
        // 返回
        return exchangeConfigItem.getId();
    }

    @Override
    public void updateExchangeConfigItem(ExchangeConfigItemSaveReqVO updateReqVO) {
        // 校验存在
        validateExchangeConfigItemExists(updateReqVO.getId());
        // 更新
        ExchangeConfigItemDO updateObj = BeanUtils.toBean(updateReqVO, ExchangeConfigItemDO.class);
        exchangeConfigItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteExchangeConfigItem(Long id) {
        // 校验存在
        validateExchangeConfigItemExists(id);
        // 删除
        exchangeConfigItemMapper.deleteById(id);
    }

    private void validateExchangeConfigItemExists(Long id) {
        if (exchangeConfigItemMapper.selectById(id) == null) {
            throw exception(EXCHANGE_CONFIG_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public ExchangeConfigItemDO getExchangeConfigItem(Long id) {
        return exchangeConfigItemMapper.selectById(id);
    }

    @Override
    public PageResult<ExchangeConfigItemDO> getExchangeConfigItemPage(ExchangeConfigItemPageReqVO pageReqVO) {
        return exchangeConfigItemMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createExchangeConfigItem(ExchangeConfigItemDO createReqVO) {
        exchangeConfigItemMapper.insert(createReqVO);
        // 返回
        return createReqVO.getId();
    }

    @Override
    public void updateExchangeConfigItem(ExchangeConfigItemDO updateReqVO) {
        // 校验存在
        validateExchangeConfigItemExists(updateReqVO.getId());

        exchangeConfigItemMapper.updateById(updateReqVO);
    }

    @Override
    public ExchangeConfigItemDO getExchangeConfigItemByCode(String code) {
        if (StringUtils.isEmpty(code)){
            return null;
        }
        return exchangeConfigItemMapper.selectOne(ExchangeConfigItemDO::getCode,code);
    }

}