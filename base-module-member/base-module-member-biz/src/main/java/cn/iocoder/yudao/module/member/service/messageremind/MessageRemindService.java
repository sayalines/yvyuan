package cn.iocoder.yudao.module.member.service.messageremind;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.messageremind.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.messageremind.MessageRemindDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 会员消息提醒 Service 接口
 *
 * @author 超级管理员
 */
public interface MessageRemindService {

    /**
     * 创建会员消息提醒
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMessageRemind(@Valid MessageRemindSaveReqVO createReqVO);

    /**
     * 更新会员消息提醒
     *
     * @param updateReqVO 更新信息
     */
    void updateMessageRemind(@Valid MessageRemindSaveReqVO updateReqVO);

    /**
     * 删除会员消息提醒
     *
     * @param id 编号
     */
    void deleteMessageRemind(Long id);

    /**
     * 获得会员消息提醒
     *
     * @param id 编号
     * @return 会员消息提醒
     */
    MessageRemindDO getMessageRemind(Long id);

    /**
     * 获得会员消息提醒分页
     *
     * @param pageReqVO 分页查询
     * @return 会员消息提醒分页
     */
    PageResult<MessageRemindDO> getMessageRemindPage(MessageRemindPageReqVO pageReqVO);

    /**
     * 获取待提醒数据
     * @return
     */
    List<MessageRemindDO> findRemindList();

    /**
     * 更新会员消息提醒
     *
     * @param entity 更新信息
     */
    void update(MessageRemindDO entity);

    /**
     * 自定义查询
     * @param wrapper
     * @return
     */
    List<MessageRemindDO> findList(LambdaQueryWrapper<MessageRemindDO> wrapper);

    /**
     * 是否做了签到提醒
     * @param userId
     * @return
     */
    Boolean isDoDaySignRemind(Long userId);

    /**
     * 是否做了新品开售提醒
     * @param userId
     * @return
     */
    Boolean isDoNewProductRemind(Long userId,Long bizId);

    /**
     * 是否做了直播开播提醒
     * @param userId
     * @return
     */
    Boolean isDoZBRemind(Long userId,Long bizId);


    /**
     * 是否做了预约活动开始提醒
     * @param userId
     * @return
     */
    Boolean isDoYyhdRemind(Long userId,Long bizId);

    /**
     * 是否做了预约到货通知
     * @param userId
     * @return
     */
    Boolean isDoYydhRemind(Long userId,Long bizId);
}