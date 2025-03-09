package cn.iocoder.yudao.module.infra.api.logger;

import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogUpdateReqDTO;
import cn.iocoder.yudao.module.infra.service.logger.ApiAccessLogService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * API 访问日志的 API 实现类
 *
 * @author 商城管理系统
 */
@Service
@Validated
public class ApiAccessLogApiImpl implements ApiAccessLogApi {

    @Resource
    private ApiAccessLogService apiAccessLogService;

    @Override
    public Long createApiAccessLog(ApiAccessLogCreateReqDTO createDTO) {
        return apiAccessLogService.createApiAccessLog(createDTO);
    }

    @Override
    public void addBatchApiAccessLog(List<ApiAccessLogCreateReqDTO> list) {
        apiAccessLogService.addBatchApiAccessLog(list);
    }

    @Override
    public void updateApiAccessLog(String traceId,String endTime) {
        apiAccessLogService.updateApiAccessLog(traceId,endTime);
    }

    @Override
    public void updateBatchApiAccessLog(List<ApiAccessLogUpdateReqDTO> list) {
        apiAccessLogService.updateBatchApiAccessLog(list);
    }

}
