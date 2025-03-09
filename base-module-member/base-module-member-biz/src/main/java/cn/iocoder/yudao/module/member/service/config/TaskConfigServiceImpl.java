package cn.iocoder.yudao.module.member.service.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.config.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.config.TaskConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.config.TaskConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 任务配置 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class TaskConfigServiceImpl implements TaskConfigService {

    @Resource
    private TaskConfigMapper taskConfigMapper;

    @Override
    public Long createTaskConfig(TaskConfigSaveReqVO createReqVO) {
        // 插入
        TaskConfigDO taskConfig = BeanUtils.toBean(createReqVO, TaskConfigDO.class);
        taskConfigMapper.insert(taskConfig);
        // 返回
        return taskConfig.getId();
    }

    @Override
    public void updateTaskConfig(TaskConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateTaskConfigExists(updateReqVO.getId());
        // 更新
        TaskConfigDO updateObj = BeanUtils.toBean(updateReqVO, TaskConfigDO.class);
        taskConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteTaskConfig(Long id) {
        // 校验存在
        validateTaskConfigExists(id);
        // 删除
        taskConfigMapper.deleteById(id);
    }

    private void validateTaskConfigExists(Long id) {
        if (taskConfigMapper.selectById(id) == null) {
            throw exception(TASK_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public TaskConfigDO getTaskConfig(Long id) {
        return taskConfigMapper.selectById(id);
    }

    @Override
    public PageResult<TaskConfigDO> getTaskConfigPage(TaskConfigPageReqVO pageReqVO) {
        return taskConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<TaskConfigDO> findList(LambdaQueryWrapper<TaskConfigDO> wrapper) {
        return taskConfigMapper.selectList(wrapper);
    }

    @Override
    public TaskConfigDO getTaskConfigByType(Integer bizType) {
        TaskConfigDO result = null;
        if (bizType==null){
            return result;
        }
        List<TaskConfigDO> list = taskConfigMapper.selectList(TaskConfigDO::getBizType,bizType);
        if (list!=null && list.size()>0){
            result = list.get(0);
        }
        return result;
    }

}