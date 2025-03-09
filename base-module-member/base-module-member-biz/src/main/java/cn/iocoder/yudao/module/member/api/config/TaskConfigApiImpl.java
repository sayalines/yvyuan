package cn.iocoder.yudao.module.member.api.config;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.api.config.dto.TaskConfigRespDTO;
import cn.iocoder.yudao.module.member.service.config.TaskConfigService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;

/**
 * 任务配置 API 实现类
 *
 * @author owen
 */
@Service
@Validated
public class TaskConfigApiImpl implements TaskConfigApi {

    @Resource
    private TaskConfigService taskConfigService;

    @Override
    public TaskConfigRespDTO getConfig(Long id) {
        return BeanUtils.toBean(taskConfigService.getTaskConfig(id),TaskConfigRespDTO.class);
    }

    @Override
    public TaskConfigRespDTO getTaskConfigByType(Integer bizType) {
        return  BeanUtils.toBean(taskConfigService.getTaskConfigByType(bizType),TaskConfigRespDTO.class);
    }

}
