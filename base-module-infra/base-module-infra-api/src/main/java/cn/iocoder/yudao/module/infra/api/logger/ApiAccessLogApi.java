package cn.iocoder.yudao.module.infra.api.logger;

import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogUpdateReqDTO;

import javax.validation.Valid;
import java.util.List;

/**
 * API 访问日志的 API 接口
 *
 * @author 商城管理系统
 */
public interface ApiAccessLogApi {

    /**
     * 创建 API 访问日志
     *
     * @param createDTO 创建信息
     */
    Long createApiAccessLog(@Valid ApiAccessLogCreateReqDTO createDTO);

    /**
     * 批量创建 API 访问日志
     *
     * @param list API 访问日志
     */
    void addBatchApiAccessLog(List<ApiAccessLogCreateReqDTO> list);

    /**
     * 更新 API 访问日志
     * @param id
     */
    void updateApiAccessLog(String traceId,String endTime);
    /**
     * 批量更新 API 访问日志
     */
    void updateBatchApiAccessLog(List<ApiAccessLogUpdateReqDTO> list);

}
