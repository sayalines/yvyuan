package cn.iocoder.yudao.module.pay.api.channel;

import cn.iocoder.yudao.module.pay.api.channel.dto.PayChannelDTO;

public interface PayChannelApi {
    /**
     * 根据条件获取渠道
     *
     * @param code       渠道编码
     * @return 数量
     */
    PayChannelDTO getChannelByCode(String code);
    /**
     * 根据条件获取渠道
     *
     * @param code       渠道编码
     * @return 数量
     */
    String getMchIdByCode(String code);
}
