package cn.iocoder.yudao.module.member.service.exchangeConfig;

import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfigItem.ExchangeConfigItemDO;
import cn.iocoder.yudao.module.member.service.exchangeConfigItem.ExchangeConfigItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.exchangeConfig.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfig.ExchangeConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.exchangeConfig.ExchangeConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 兑换配置 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class ExchangeConfigServiceImpl implements ExchangeConfigService {

    @Resource
    private ExchangeConfigMapper exchangeConfigMapper;
    @Resource
    private ExchangeConfigItemService exchangeConfigItemService;

    @Override
    public Long createExchangeConfig(ExchangeConfigSaveReqVO createReqVO) {
        // 插入
        ExchangeConfigDO exchangeConfig = BeanUtils.toBean(createReqVO, ExchangeConfigDO.class);
        exchangeConfigMapper.insert(exchangeConfig);
        // 返回
        return exchangeConfig.getId();
    }

    @Override
    public void updateExchangeConfig(ExchangeConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateExchangeConfigExists(updateReqVO.getId());
        // 更新
        ExchangeConfigDO updateObj = BeanUtils.toBean(updateReqVO, ExchangeConfigDO.class);
        exchangeConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteExchangeConfig(Long id) {
        // 校验存在
        validateExchangeConfigExists(id);
        // 删除
        exchangeConfigMapper.deleteById(id);
    }

    private void validateExchangeConfigExists(Long id) {
        if (exchangeConfigMapper.selectById(id) == null) {
            throw exception(EXCHANGE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public ExchangeConfigDO getExchangeConfig(Long id) {
        return exchangeConfigMapper.selectById(id);
    }

    @Override
    public PageResult<ExchangeConfigDO> getExchangeConfigPage(ExchangeConfigPageReqVO pageReqVO) {
        return exchangeConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public void createExchange(Long configId, Integer count) {
        if (configId==null || count==null){
            throw exception(EXCHANGE_CONFIG_NOT_PARAM);
        }

        if (count.compareTo(1)<0){
            count = 1;
        }

        ExchangeConfigDO configDO = exchangeConfigMapper.selectById(configId);
        if (configDO==null){
            throw exception(EXCHANGE_CONFIG_NOT_EXISTS);
        }

        Integer codeLength = configDO.getCodeLength();
        if (codeLength==null){
            codeLength = 0;
        }

        Integer randomCodeLength = codeLength;
        if (StringUtils.isNotEmpty(configDO.getPrefix())){
            randomCodeLength = codeLength-configDO.getPrefix().length();
        }

        if (randomCodeLength<8){
            randomCodeLength = 8;
        }

        for(int i=0;i<count;i++){
            String code = "";
            Boolean isOk = false;
            while (!isOk){
                if (StringUtils.isNotEmpty(configDO.getPrefix())){
                    code = code+configDO.getPrefix();
                }
                code = code+RandomUtil.randomNumbers(randomCodeLength);
                //判断兑换码是否重复
                ExchangeConfigItemDO itemDO = exchangeConfigItemService.getExchangeConfigItemByCode(code);
                if (itemDO==null){
                    isOk = true;
                }
            }

            ExchangeConfigItemDO exchangeConfigItemDO = new ExchangeConfigItemDO();
            exchangeConfigItemDO.setConfigId(configDO.getId());
            exchangeConfigItemDO.setCode(code);
            exchangeConfigItemDO.setBizType(configDO.getBizType());
            exchangeConfigItemDO.setTotalCount(configDO.getUseCount());
            exchangeConfigItemDO.setUseCount(0);
            exchangeConfigItemDO.setValidStartTime(configDO.getValidStartTime());
            exchangeConfigItemDO.setValidEndTime(configDO.getValidEndTime());
            exchangeConfigItemDO.setBizId(configDO.getBizId());
            exchangeConfigItemService.createExchangeConfigItem(exchangeConfigItemDO);
        }
    }

}