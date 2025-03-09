package cn.iocoder.yudao.module.system.controller.admin.question;

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

import cn.iocoder.yudao.module.system.controller.admin.question.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.question.QuestionDO;
import cn.iocoder.yudao.module.system.service.question.QuestionService;

@Tag(name = "管理后台 - 量表管理")
@RestController
@RequestMapping("/system/question")
@Validated
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @PostMapping("/create")
    @Operation(summary = "创建量表管理")
    @PreAuthorize("@ss.hasPermission('system:question:create')")
    public CommonResult<Long> createQuestion(@Valid @RequestBody QuestionSaveReqVO createReqVO) {
        return success(questionService.createQuestion(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新量表管理")
    @PreAuthorize("@ss.hasPermission('system:question:update')")
    public CommonResult<Boolean> updateQuestion(@Valid @RequestBody QuestionSaveReqVO updateReqVO) {
        questionService.updateQuestion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除量表管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:question:delete')")
    public CommonResult<Boolean> deleteQuestion(@RequestParam("id") Long id) {
        questionService.deleteQuestion(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得量表管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<QuestionRespVO> getQuestion(@RequestParam("id") Long id) {
        QuestionDO question = questionService.getQuestion(id);
        return success(BeanUtils.toBean(question, QuestionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得量表管理分页")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<PageResult<QuestionRespVO>> getQuestionPage(@Valid QuestionPageReqVO pageReqVO) {
        PageResult<QuestionDO> pageResult = questionService.getQuestionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, QuestionRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得量表管理列表")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<List<QuestionRespVO>> getQuestionList() {
        List<QuestionRespVO> list = new ArrayList<>();
        QuestionPageReqVO pageReqVO = new QuestionPageReqVO();
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<QuestionRespVO> pageResult = BeanUtils.toBean(questionService.getQuestionPage(pageReqVO), QuestionRespVO.class);
        if (pageResult!=null && pageResult.getList()!=null && pageResult.getList().size()>0){
            list = pageResult.getList();
        }
        return success(list);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出量表管理 Excel")
    @PreAuthorize("@ss.hasPermission('system:question:export')")
    @OperateLog(type = EXPORT)
    public void exportQuestionExcel(@Valid QuestionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<QuestionDO> list = questionService.getQuestionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "量表管理.xls", "数据", QuestionRespVO.class,
                        BeanUtils.toBean(list, QuestionRespVO.class));
    }

}