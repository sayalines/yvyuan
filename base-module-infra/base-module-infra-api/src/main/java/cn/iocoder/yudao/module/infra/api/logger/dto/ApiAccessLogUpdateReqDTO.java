package cn.iocoder.yudao.module.infra.api.logger.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiAccessLogUpdateReqDTO {
    /**
     * 链路追踪编号
     */
    private String traceId;
    /**
     * 结束请求时间
     */
    private String endTime;
}
