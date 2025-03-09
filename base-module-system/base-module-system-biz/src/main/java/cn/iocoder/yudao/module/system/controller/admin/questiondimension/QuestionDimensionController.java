package cn.iocoder.yudao.module.system.controller.admin.questiondimension;

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

import cn.iocoder.yudao.module.system.controller.admin.questiondimension.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questiondimension.QuestionDimensionDO;
import cn.iocoder.yudao.module.system.service.questiondimension.QuestionDimensionService;

@Tag(name = "管理后台 - 量表维度")
@RestController
@RequestMapping("/system/question-dimension")
@Validated
public class QuestionDimensionController {

    @Resource
    private QuestionDimensionService questionDimensionService;

    @PostMapping("/create")
    @Operation(summary = "创建量表维度")
    @PreAuthorize("@ss.hasPermission('system:question:create')")
    public CommonResult<Long> createQuestionDimension(@Valid @RequestBody QuestionDimensionSaveReqVO createReqVO) {
        return success(questionDimensionService.createQuestionDimension(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新量表维度")
    @PreAuthorize("@ss.hasPermission('system:question:update')")
    public CommonResult<Boolean> updateQuestionDimension(@Valid @RequestBody QuestionDimensionSaveReqVO updateReqVO) {
        questionDimensionService.updateQuestionDimension(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除量表维度")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:question:delete')")
    public CommonResult<Boolean> deleteQuestionDimension(@RequestParam("id") Long id) {
        questionDimensionService.deleteQuestionDimension(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得量表维度")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<QuestionDimensionRespVO> getQuestionDimension(@RequestParam("id") Long id) {
        QuestionDimensionDO questionDimension = questionDimensionService.getQuestionDimension(id);
        return success(BeanUtils.toBean(questionDimension, QuestionDimensionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得量表维度分页")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<PageResult<QuestionDimensionRespVO>> getQuestionDimensionPage(@Valid QuestionDimensionPageReqVO pageReqVO) {
        PageResult<QuestionDimensionDO> pageResult = questionDimensionService.getQuestionDimensionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, QuestionDimensionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出量表维度 Excel")
    @PreAuthorize("@ss.hasPermission('system:question:export')")
    @OperateLog(type = EXPORT)
    public void exportQuestionDimensionExcel(@Valid QuestionDimensionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<QuestionDimensionDO> list = questionDimensionService.getQuestionDimensionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "量表维度.xls", "数据", QuestionDimensionRespVO.class,
                        BeanUtils.toBean(list, QuestionDimensionRespVO.class));
    }

}