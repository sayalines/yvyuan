package cn.iocoder.yudao.module.pay.api.channel;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pay.api.channel.dto.PayChannelDTO;
import cn.iocoder.yudao.module.pay.controller.admin.channel.vo.PayChannelRespVO;
import cn.iocoder.yudao.module.pay.service.channel.PayChannelService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PayChannelApiImpl implements PayChannelApi{
    @Resource
    private PayChannelService channelService;
    @Override
    public PayChannelDTO getChannelByCode(String code) {
        return BeanUtils.toBean(channelService.getChannelByCode(code),PayChannelDTO.class);
    }

    @Override
    public String getMchIdByCode(String code) {
        PayChannelRespVO dto = channelService.getChannelByCode(code);
        if (dto!=null){
            String config = dto.getConfig();
            if (StringUtils.isNotEmpty(config)){
                try{
                    JSONObject obj = JSONObject.parseObject(config);
                    if (obj!=null && obj.containsKey("mchId")){
                        return obj.getString("mchId");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
