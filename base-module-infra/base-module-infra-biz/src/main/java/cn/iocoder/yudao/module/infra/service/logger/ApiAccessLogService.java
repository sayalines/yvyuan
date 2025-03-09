package cn.iocoder.yudao.module.infra.service.logger;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogCreateReqDTO;
import cn.iocoder.yudao.module.infra.api.logger.dto.ApiAccessLogUpdateReqDTO;
import cn.iocoder.yudao.module.infra.controller.admin.logger.vo.apiaccesslog.ApiAccessLogPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.logger.ApiAccessLogDO;

import java.util.List;

/**
 * API 访问日志 Service 接口
 *
 * @author 商城管理系统
 */
public interface ApiAccessLogService {

    /**
     * 创建 API 访问日志
     *
     * @param createReqDTO API 访问日志
     */
    Long createApiAccessLog(ApiAccessLogCreateReqDTO createReqDTO);

    /**
     * 批量创建 API 访问日志
     *
     * @param list API 访问日志
     */
    void addBatchApiAccessLog(List<ApiAccessLogCreateReqDTO> list);

    /**
     * 更新 API 访问日志
     */
    void updateApiAccessLog(String traceId,String endTime);

    /**
     * 批量更新 API 访问日志
     */
    void updateBatchApiAccessLog(List<ApiAccessLogUpdateReqDTO> list);

    /**
     * 获得 API 访问日志分页
     *
     * @param pageReqVO 分页查询
     * @return API 访问日志分页
     */
    PageResult<ApiAccessLogDO> getApiAccessLogPage(ApiAccessLogPageReqVO pageReqVO);

    /**
     * 清理 exceedDay 天前的访问日志
     *
     * @param exceedDay 超过多少天就进行清理
     * @param deleteLimit 清理的间隔条数
     */
    Integer cleanAccessLog(Integer exceedDay, Integer deleteLimit);

}
