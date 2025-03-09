package cn.iocoder.yudao.module.member.controller.admin.productvisitlog;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.admin.productvisitlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.productvisitlog.ProductVisitLogDO;
import cn.iocoder.yudao.module.member.service.productvisitlog.ProductVisitLogService;

@Tag(name = "管理后台 - 商品访问日志")
@RestController
@RequestMapping("/member/product-visit-log")
@Validated
public class ProductVisitLogController {

    @Resource
    private ProductVisitLogService productVisitLogService;

    @PostMapping("/create")
    @Operation(summary = "创建商品访问日志")
    @PreAuthorize("@ss.hasPermission('member:product-visit-log:create')")
    public CommonResult<Long> createProductVisitLog(@Valid @RequestBody ProductVisitLogSaveReqVO createReqVO) {
        return success(productVisitLogService.createProductVisitLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品访问日志")
    @PreAuthorize("@ss.hasPermission('member:product-visit-log:update')")
    public CommonResult<Boolean> updateProductVisitLog(@Valid @RequestBody ProductVisitLogSaveReqVO updateReqVO) {
        productVisitLogService.updateProductVisitLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品访问日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:product-visit-log:delete')")
    public CommonResult<Boolean> deleteProductVisitLog(@RequestParam("id") Long id) {
        productVisitLogService.deleteProductVisitLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商品访问日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:product-visit-log:query')")
    public CommonResult<ProductVisitLogRespVO> getProductVisitLog(@RequestParam("id") Long id) {
        ProductVisitLogDO productVisitLog = productVisitLogService.getProductVisitLog(id);
        return success(BeanUtils.toBean(productVisitLog, ProductVisitLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品访问日志分页")
    @PreAuthorize("@ss.hasPermission('member:product-visit-log:query')")
    public CommonResult<PageResult<ProductVisitLogRespVO>> getProductVisitLogPage(@Valid ProductVisitLogPageReqVO pageReqVO) {
        PageResult<ProductVisitLogDO> pageResult = productVisitLogService.getProductVisitLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductVisitLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出商品访问日志 Excel")
    @PreAuthorize("@ss.hasPermission('member:product-visit-log:export')")
    @OperateLog(type = EXPORT)
    public void exportProductVisitLogExcel(@Valid ProductVisitLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductVisitLogDO> list = productVisitLogService.getProductVisitLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "商品访问日志.xls", "数据", ProductVisitLogRespVO.class,
                        BeanUtils.toBean(list, ProductVisitLogRespVO.class));
    }

}