package cn.iocoder.yudao.module.statistics.controller.admin.common;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.CommonPageReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.OtherRespVO;
import cn.iocoder.yudao.module.statistics.service.common.CommonStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 常用统计")
@RestController
@RequestMapping("/statistics")
@Validated
@Slf4j
public class CommonStatisticsController {
    @Resource
    private CommonStatisticsService commonStatisticsService;

    @GetMapping("/other/page")
    @Operation(summary = "获得其他统计分页")
    @PreAuthorize("@ss.hasPermission('statistics:other:query')")
    public CommonResult<PageResult<OtherRespVO>> getOtherPage(@Valid CommonPageReqVO pageReqVO) {
        return success(commonStatisticsService.getOtherage(pageReqVO));
    }

    @GetMapping("/other/export-excel")
    @Operation(summary = "导出其他统计 Excel")
    @PreAuthorize("@ss.hasPermission('statistics:other:export')")
    public void exportOtherExcel(CommonPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OtherRespVO> list = commonStatisticsService.getOtherage(pageReqVO).getList();
        ExcelUtils.write(response, "其他数据.xls", "统计", OtherRespVO.class, list);
    }

}
