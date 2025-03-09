package cn.iocoder.yudao.module.system.controller.admin.questiontopic;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.alibaba.excel.util.StringUtils;
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

import cn.iocoder.yudao.module.system.controller.admin.questiontopic.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questiontopic.QuestionTopicDO;
import cn.iocoder.yudao.module.system.service.questiontopic.QuestionTopicService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 量表题目")
@RestController
@RequestMapping("/system/question-topic")
@Validated
public class QuestionTopicController {

    @Resource
    private QuestionTopicService questionTopicService;

    @PostMapping("/create")
    @Operation(summary = "创建量表题目")
    @PreAuthorize("@ss.hasPermission('system:question:create')")
    public CommonResult<Long> createQuestionTopic(@Valid @RequestBody QuestionTopicSaveReqVO createReqVO) {
        return success(questionTopicService.createQuestionTopic(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新量表题目")
    @PreAuthorize("@ss.hasPermission('system:question:update')")
    public CommonResult<Boolean> updateQuestionTopic(@Valid @RequestBody QuestionTopicSaveReqVO updateReqVO) {
        questionTopicService.updateQuestionTopic(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/copy")
    @Operation(summary = "复制量表题目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:question:create')")
    public CommonResult<Boolean> copyQuestionTopic(@RequestParam("id") Long id) {
        questionTopicService.copyQuestionTopic(id);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除量表题目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:question:delete')")
    public CommonResult<Boolean> deleteQuestionTopic(@RequestParam("id") Long id) {
        questionTopicService.deleteQuestionTopic(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得量表题目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<QuestionTopicRespVO> getQuestionTopic(@RequestParam("id") Long id) {
        QuestionTopicDO questionTopic = questionTopicService.getQuestionTopic(id);
        return success(BeanUtils.toBean(questionTopic, QuestionTopicRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得量表题目分页")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<PageResult<QuestionTopicRespVO>> getQuestionTopicPage(@Valid QuestionTopicPageReqVO pageReqVO) {
        PageResult<QuestionTopicDO> pageResult = questionTopicService.getQuestionTopicPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, QuestionTopicRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得量表题目列表")
    @PreAuthorize("@ss.hasPermission('system:question:query')")
    public CommonResult<List<QuestionTopicRespVO>> getQuestionTopicList(Long questionId) {
        QuestionTopicPageReqVO pageReqVO = new QuestionTopicPageReqVO();
        pageReqVO.setQuestionId(questionId);
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<QuestionTopicDO> pageResult = questionTopicService.getQuestionTopicPage(pageReqVO);
        List<QuestionTopicRespVO> list = new ArrayList<>();
        if (pageResult!=null && pageResult.getList().size()>0){
            list = BeanUtils.toBean(pageResult.getList(), QuestionTopicRespVO.class);
            for(QuestionTopicRespVO dd:list){
                String calcName = dd.getName();
                if (StringUtils.isNotBlank(dd.getCode())){
                    calcName = dd.getCode()+"."+calcName;
                }
                dd.setCalcName(calcName);
            }
        }
        return success(list);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出量表题目 Excel")
    @PreAuthorize("@ss.hasPermission('system:question:export')")
    @OperateLog(type = EXPORT)
    public void exportQuestionTopicExcel(@Valid QuestionTopicPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<QuestionTopicDO> list = questionTopicService.getQuestionTopicPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "量表题目.xls", "数据", QuestionTopicRespVO.class,
                        BeanUtils.toBean(list, QuestionTopicRespVO.class));
    }

    @GetMapping("/import-excel-moudle")
    @Operation(summary = "获得导入量表题目模板")
    public void importQuestionTopicExcelMoudle(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<QuestionTopicImportVO> list = Arrays.asList(
                QuestionTopicImportVO.builder().code("1").name("大致说来样样事情都开心。").type(1).orderNo(1)
                        .optValue1("A").optContent1("是").optScore1(1).optValue2("B").optContent2("否").optScore2(0).build()
        );
        // 输出
        ExcelUtils.write(response, "量表题目模板.xls", "量表题目列表", QuestionTopicImportVO.class, list);
    }

    @PostMapping("/excel/{id}")
    @Operation(summary = "导入量表题目 Excel")
    @PreAuthorize("@ss.hasPermission('system:question:create')")
    @OperateLog(type = EXPORT)
    public CommonResult GroupExcel(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id, Boolean updateSupport) {
        questionTopicService.importExcel(file, id, updateSupport);
        CommonResult result = new CommonResult();
        result.setCode(GlobalErrorCodeConstants.SUCCESS.getCode());
        result.setMsg("导入成功");
        return result;
    }

}