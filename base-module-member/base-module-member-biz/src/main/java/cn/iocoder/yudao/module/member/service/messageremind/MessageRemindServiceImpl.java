package cn.iocoder.yudao.module.member.service.messageremind;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.messageremind.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.messageremind.MessageRemindDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.dal.mysql.messageremind.MessageRemindMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 会员消息提醒 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class MessageRemindServiceImpl implements MessageRemindService {

    @Resource
    private MessageRemindMapper messageRemindMapper;

    @Override
    public Long createMessageRemind(MessageRemindSaveReqVO createReqVO) {
        // 插入
        MessageRemindDO messageRemind = BeanUtils.toBean(createReqVO, MessageRemindDO.class);
        messageRemindMapper.insert(messageRemind);
        // 返回
        return messageRemind.getId();
    }

    @Override
    public void updateMessageRemind(MessageRemindSaveReqVO updateReqVO) {
        // 校验存在
        validateMessageRemindExists(updateReqVO.getId());
        // 更新
        MessageRemindDO updateObj = BeanUtils.toBean(updateReqVO, MessageRemindDO.class);
        messageRemindMapper.updateById(updateObj);
    }

    @Override
    public void deleteMessageRemind(Long id) {
        // 校验存在
        validateMessageRemindExists(id);
        // 删除
        messageRemindMapper.deleteById(id);
    }

    private void validateMessageRemindExists(Long id) {
        if (messageRemindMapper.selectById(id) == null) {
            throw exception(MESSAGE_REMIND_NOT_EXISTS);
        }
    }

    @Override
    public MessageRemindDO getMessageRemind(Long id) {
        return messageRemindMapper.selectById(id);
    }

    @Override
    public PageResult<MessageRemindDO> getMessageRemindPage(MessageRemindPageReqVO pageReqVO) {
        return messageRemindMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MessageRemindDO> findRemindList() {
        LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(MessageRemindDO::getIsRemind,false)
                .le(MessageRemindDO::getRemindTime, LocalDateTime.now());
        List<MessageRemindDO> list = messageRemindMapper.selectList(wrapper);
        if (list==null){
            list = new ArrayList<>();
        }

        wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(MessageRemindDO::getIsRemind,false).eq(MessageRemindDO::getBizType,1);
        List<MessageRemindDO> list02 = messageRemindMapper.selectList(wrapper);
        if (list02!=null && list02.size()>0){
            list.addAll(list02);
        }
        return list;
    }

    @Override
    public void update(MessageRemindDO entity) {
        messageRemindMapper.updateById(entity);
    }

    @Override
    public List<MessageRemindDO> findList(LambdaQueryWrapper<MessageRemindDO> wrapper) {
        return messageRemindMapper.selectList(wrapper);
    }

    @Override
    public Boolean isDoDaySignRemind(Long userId) {
        Boolean result = false;
        LocalDateTime queryDate = LocalDateTime.now().plusDays(1);
        LocalDateTime startTime = toFormatLocalDateTime(queryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 00:00:00");
        LocalDateTime endTime =toFormatLocalDateTime(queryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" 23:59:59");
        LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(MessageRemindDO::getUserId,userId).eq(MessageRemindDO::getBizType,8)
                .eq(MessageRemindDO::getIsRemind,false)
                .between(MessageRemindDO::getRemindTime,startTime,endTime);
        Long res = messageRemindMapper.selectCount(wrapper);
        if (res!=null && res.compareTo(0L)>0){
            result = true;
        }
        return result;
    }

    @Override
    public Boolean isDoNewProductRemind(Long userId,Long bizId) {
        Boolean result = false;
        LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(MessageRemindDO::getUserId,userId).eq(MessageRemindDO::getBizType,2)
                .eq(MessageRemindDO::getIsRemind,false)
                .eq(MessageRemindDO::getBizId,bizId);
        Long res = messageRemindMapper.selectCount(wrapper);
        if (res!=null && res.compareTo(0L)>0){
            result = true;
        }
        return result;
    }

    @Override
    public Boolean isDoZBRemind(Long userId, Long bizId) {
        Boolean result = false;
        LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(MessageRemindDO::getUserId,userId).eq(MessageRemindDO::getBizType,7)
                .eq(MessageRemindDO::getIsRemind,false)
                .eq(MessageRemindDO::getBizId,bizId);
        Long res = messageRemindMapper.selectCount(wrapper);
        if (res!=null && res.compareTo(0L)>0){
            result = true;
        }
        return result;
    }

    @Override
    public Boolean isDoYyhdRemind(Long userId, Long bizId) {
        Boolean result = false;
        if (userId==null){
            return result;
        }
        LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(MessageRemindDO::getUserId,userId).eq(MessageRemindDO::getBizType,5)
                .eq(MessageRemindDO::getIsRemind,false)
                .eq(MessageRemindDO::getBizId,bizId);
        Long res = messageRemindMapper.selectCount(wrapper);
        if (res!=null && res.compareTo(0L)>0){
            result = true;
        }
        return result;
    }

    @Override
    public Boolean isDoYydhRemind(Long userId, Long bizId) {
        Boolean result = false;
        LambdaQueryWrapper<MessageRemindDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(MessageRemindDO::getUserId,userId).eq(MessageRemindDO::getBizType,1)
                .eq(MessageRemindDO::getIsRemind,false)
                .eq(MessageRemindDO::getBizId,bizId);
        Long res = messageRemindMapper.selectCount(wrapper);
        if (res!=null && res.compareTo(0L)>0){
            result = true;
        }
        return result;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public LocalDateTime toFormatLocalDateTime(String dateTime){
        LocalDateTime result = null;
        if (StringUtils.isNotEmpty(dateTime)){
            String formatter = "yyyy-MM-dd HH:mm:ss";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
            result = LocalDateTime.parse(dateTime,df);
        }
        return result;
    }

}