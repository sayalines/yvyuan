package cn.iocoder.yudao.module.member.controller.admin.feedback;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.member.controller.admin.feedback.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.feedback.MemberFeedbackDO;
import cn.iocoder.yudao.module.member.service.feedback.MemberFeedbackService;

@Tag(name = "管理后台 - 问题反馈")
@RestController
@RequestMapping("/member/question")
@Validated
public class MemberFeedbackController {

    @Resource
    private MemberFeedbackService memberFeedbackService;

    @PostMapping("/create")
    @Operation(summary = "创建问题反馈")
    @PreAuthorize("@ss.hasPermission('member:question:create')")
    public CommonResult<Long> createQuestion(@Valid @RequestBody MemberFeedbackSaveReqVO createReqVO) {
        return success(memberFeedbackService.createQuestion(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新问题反馈")
    @PreAuthorize("@ss.hasPermission('member:question:update')")
    public CommonResult<Boolean> updateQuestion(@Valid @RequestBody MemberFeedbackSaveReqVO updateReqVO) {
        memberFeedbackService.updateQuestion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除问题反馈")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:question:delete')")
    public CommonResult<Boolean> deleteQuestion(@RequestParam("id") Long id) {
        memberFeedbackService.deleteQuestion(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得问题反馈")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:question:query')")
    public CommonResult<MemberFeedbackRespVO> getQuestion(@RequestParam("id") Long id) {
        MemberFeedbackDO question = memberFeedbackService.getQuestion(id);
        return success(BeanUtils.toBean(question, MemberFeedbackRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得问题反馈分页")
    @PreAuthorize("@ss.hasPermission('member:question:query')")
    public CommonResult<PageResult<MemberFeedbackRespVO>> getQuestionPage(@Valid MemberFeedbackPageReqVO pageReqVO) {
        PageResult<MemberFeedbackDO> pageResult = memberFeedbackService.getQuestionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MemberFeedbackRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出问题反馈 Excel")
    @PreAuthorize("@ss.hasPermission('member:question:export')")
    @OperateLog(type = EXPORT)
    public void exportQuestionExcel(@Valid MemberFeedbackPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MemberFeedbackDO> list = memberFeedbackService.getQuestionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "问题反馈.xls", "数据", MemberFeedbackRespVO.class,
                        BeanUtils.toBean(list, MemberFeedbackRespVO.class));
    }

    @PutMapping("/audit/{id}")
    @Operation(summary = "更改解决状态")
    @PreAuthorize("@ss.hasPermission('member:question:update')")
    public CommonResult<Boolean> auditQuestion(@Valid @RequestBody MemberFeedbackAuditReqVO auditReqVO) {
        memberFeedbackService.auditQuestion(auditReqVO.getId(), auditReqVO.getResult());
        return success(true);
    }
}