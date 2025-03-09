package cn.iocoder.yudao.module.member.service.config;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.config.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.config.TaskConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 任务配置 Service 接口
 *
 * @author 超级管理员
 */
public interface TaskConfigService {

    /**
     * 创建任务配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTaskConfig(@Valid TaskConfigSaveReqVO createReqVO);

    /**
     * 更新任务配置
     *
     * @param updateReqVO 更新信息
     */
    void updateTaskConfig(@Valid TaskConfigSaveReqVO updateReqVO);

    /**
     * 删除任务配置
     *
     * @param id 编号
     */
    void deleteTaskConfig(Long id);

    /**
     * 获得任务配置
     *
     * @param id 编号
     * @return 任务配置
     */
    TaskConfigDO getTaskConfig(Long id);

    /**
     * 获得任务配置分页
     *
     * @param pageReqVO 分页查询
     * @return 任务配置分页
     */
    PageResult<TaskConfigDO> getTaskConfigPage(TaskConfigPageReqVO pageReqVO);

    List<TaskConfigDO> findList(LambdaQueryWrapper<TaskConfigDO> wrapper);

    /**
     * 根据业务类型获取任务
     * @param bizType
     * @return
     */
    TaskConfigDO getTaskConfigByType(Integer bizType);

}