package cn.iocoder.yudao.module.statistics.service.common;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.CommonPageReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.OtherRespVO;

/**
 * 常用统计
 */
public interface CommonStatisticsService {
    /**
     *获得其他汇总统计分页
     * @param pageReqVO
     * @return
     */
    PageResult<OtherRespVO> getOtherage(CommonPageReqVO pageReqVO);
}
