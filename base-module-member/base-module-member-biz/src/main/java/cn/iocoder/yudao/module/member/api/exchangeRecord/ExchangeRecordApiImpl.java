package cn.iocoder.yudao.module.member.api.exchangeRecord;


import cn.iocoder.yudao.module.member.service.exchangeRecord.ExchangeRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * 兑换记录 API 实现类
 *
 * @author owen
 */
@Service
@Validated
public class ExchangeRecordApiImpl implements ExchangeRecordApi {

    @Resource
    private ExchangeRecordService exchangeRecordService;


    @Override
    public void useExchangeRecord(Long userId, String code) {
        if (userId!=null && StringUtils.isNotEmpty(code)){
            exchangeRecordService.useExchangeRecord(userId,code);
        }
    }

    @Override
    public void cancelExchangeRecord(Long userId, String code) {
        if (userId!=null && StringUtils.isNotEmpty(code)){
            exchangeRecordService.cancelExchangeRecord(userId,code);
        }
    }
}
