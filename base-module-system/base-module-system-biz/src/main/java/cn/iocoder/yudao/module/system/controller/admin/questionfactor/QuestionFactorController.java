package cn.iocoder.yudao.module.system.controller.admin.questionfactor;

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

import cn.iocoder.yudao.module.system.controller.admin.questionfactor.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questionfactor.QuestionFactorDO;
import cn.iocoder.yudao.module.system.service.questionfactor.QuestionFactorService;

@Tag(name = "管理后台 - 量表因子")
@RestController
@RequestMapping("/system/question-factor")
@Validated
public class QuestionFactorController {

    @Resource
    private QuestionFactorService questionFactorService;

    @PostMapping("/create")
    @Operation(summary = "创建量表因子")
    @PreAuthorize("@ss.hasPermission('system:question:create')")
    public CommonResult<Long> createQuestionFactor(@Valid @RequestBody QuestionFactorSaveReqVO createReqVO) {
        return success(questionFactorService.createQuestionFactor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新量表因子")
    @PreAuthorize("@ss.hasPermission('system:question:update')")
    public CommonResult<Boolean> updateQuestionFactor(@Valid @RequestBody QuestionFactorSaveReqVO updateReqVO) {
        questionFactorService.updateQuestionFactor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除量表因子")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:question:delete')")
    public CommonResult<Boolean> deleteQuestionFactor(@RequestParam("id") Long id) {
        questionFactorService.deleteQuestionFactor(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得量表因子")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<QuestionFactorRespVO> getQuestionFactor(@RequestParam("id") Long id) {
        QuestionFactorDO questionFactor = questionFactorService.getQuestionFactor(id);
        return success(BeanUtils.toBean(questionFactor, QuestionFactorRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得量表因子分页")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<PageResult<QuestionFactorRespVO>> getQuestionFactorPage(@Valid QuestionFactorPageReqVO pageReqVO) {
        PageResult<QuestionFactorDO> pageResult = questionFactorService.getQuestionFactorPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, QuestionFactorRespVO.class));
    }
    @GetMapping("/list")
    @Operation(summary = "获得量表因子列表")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<List<QuestionFactorRespVO>> getQuestionFactorList(Long questionId) {
        QuestionFactorPageReqVO pageReqVO = new QuestionFactorPageReqVO();
        pageReqVO.setQuestionId(questionId);
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<QuestionFactorDO> pageResult = questionFactorService.getQuestionFactorPage(pageReqVO);
        List<QuestionFactorRespVO> list = new ArrayList<>();
        if (pageResult!=null && pageResult.getList().size()>0){
            list = BeanUtils.toBean(pageResult.getList(), QuestionFactorRespVO.class);
        }
        return success(list);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出量表因子 Excel")
    @PreAuthorize("@ss.hasPermission('system:question:export')")
    @OperateLog(type = EXPORT)
    public void exportQuestionFactorExcel(@Valid QuestionFactorPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<QuestionFactorDO> list = questionFactorService.getQuestionFactorPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "量表因子.xls", "数据", QuestionFactorRespVO.class,
                        BeanUtils.toBean(list, QuestionFactorRespVO.class));
    }

}