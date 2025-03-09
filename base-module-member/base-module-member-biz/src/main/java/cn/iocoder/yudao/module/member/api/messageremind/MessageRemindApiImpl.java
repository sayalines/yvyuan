package cn.iocoder.yudao.module.member.api.messageremind;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.messageremind.MessageRemindDO;
import cn.iocoder.yudao.module.member.service.messageremind.MessageRemindService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 会员消息提醒 API 实现类
 *
 * @author 商城管理系统
 */
@Service
@Validated
public class MessageRemindApiImpl implements MessageRemindApi {

    @Resource
    private MessageRemindService messageRemindService;


    @Override
    public MessageRemindRespDTO getMessageRemind(Long id) {
        MessageRemindDO messageRemindDO = messageRemindService.getMessageRemind(id);
        return BeanUtils.toBean(messageRemindDO,MessageRemindRespDTO.class);
    }

    @Override
    public void updateYydhRecord(List<Long> skuIds) {
        if (skuIds.size()>0){
            LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
            wrapper = wrapper.eq(MessageRemindDO::getBizType,1)
                    .in(MessageRemindDO::getBizId,skuIds)
                    .eq(MessageRemindDO::getIsRemind,false)
                    .isNull(MessageRemindDO::getRemindTime);
            List<MessageRemindDO> list = messageRemindService.findList(wrapper);
            if (list!=null && list.size()>0){
                for(MessageRemindDO dd:list){
                    dd.setRemindTime(LocalDateTime.now());
                    messageRemindService.update(dd);
                }
            }
        }
    }

    @Override
    public void updateDdfhRecord(Long orderId) {
        if (orderId!=null){
            LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
            wrapper = wrapper.eq(MessageRemindDO::getBizType,4)
                    .eq(MessageRemindDO::getBizId,orderId)
                    .eq(MessageRemindDO::getIsRemind,false)
                    .isNull(MessageRemindDO::getRemindTime);
            List<MessageRemindDO> list = messageRemindService.findList(wrapper);
            if (list!=null && list.size()>0){
                for(MessageRemindDO dd:list){
                    dd.setRemindTime(LocalDateTime.now());
                    messageRemindService.update(dd);
                }
            }
        }
    }

    @Override
    public void update(MessageRemindRespDTO dto) {
        messageRemindService.update(BeanUtils.toBean(dto,MessageRemindDO.class));
    }
}
