package cn.iocoder.yudao.module.member.api.config;

import cn.iocoder.yudao.module.member.api.config.dto.TaskConfigRespDTO;

/**
 *任务配置 API 接口
 *
 * @author owen
 */
public interface TaskConfigApi {

    /**
     * 获得任务配置
     *
     * @return 任务配置
     */
    TaskConfigRespDTO getConfig(Long id);
    /**
     * 根据业务类型获取任务
     * @param bizType
     * @return
     */
    TaskConfigRespDTO getTaskConfigByType(Integer bizType);
}
