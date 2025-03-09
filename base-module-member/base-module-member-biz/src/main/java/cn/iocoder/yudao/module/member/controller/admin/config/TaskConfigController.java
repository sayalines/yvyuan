package cn.iocoder.yudao.module.member.controller.admin.config;

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

import cn.iocoder.yudao.module.member.controller.admin.config.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.config.TaskConfigDO;
import cn.iocoder.yudao.module.member.service.config.TaskConfigService;

@Tag(name = "管理后台 - 任务配置")
@RestController
@RequestMapping("/member/task-config")
@Validated
public class TaskConfigController {

    @Resource
    private TaskConfigService taskConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建任务配置")
    @PreAuthorize("@ss.hasPermission('member:task-config:create')")
    public CommonResult<Long> createTaskConfig(@Valid @RequestBody TaskConfigSaveReqVO createReqVO) {
        return success(taskConfigService.createTaskConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新任务配置")
    @PreAuthorize("@ss.hasPermission('member:task-config:update')")
    public CommonResult<Boolean> updateTaskConfig(@Valid @RequestBody TaskConfigSaveReqVO updateReqVO) {
        taskConfigService.updateTaskConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除任务配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:task-config:delete')")
    public CommonResult<Boolean> deleteTaskConfig(@RequestParam("id") Long id) {
        taskConfigService.deleteTaskConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得任务配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:task-config:query')")
    public CommonResult<TaskConfigRespVO> getTaskConfig(@RequestParam("id") Long id) {
        TaskConfigDO taskConfig = taskConfigService.getTaskConfig(id);
        return success(BeanUtils.toBean(taskConfig, TaskConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得任务配置分页")
    @PreAuthorize("@ss.hasPermission('member:task-config:query')")
    public CommonResult<PageResult<TaskConfigRespVO>> getTaskConfigPage(@Valid TaskConfigPageReqVO pageReqVO) {
        PageResult<TaskConfigDO> pageResult = taskConfigService.getTaskConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TaskConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出任务配置 Excel")
    @PreAuthorize("@ss.hasPermission('member:task-config:export')")
    @OperateLog(type = EXPORT)
    public void exportTaskConfigExcel(@Valid TaskConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TaskConfigDO> list = taskConfigService.getTaskConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "任务配置.xls", "数据", TaskConfigRespVO.class,
                        BeanUtils.toBean(list, TaskConfigRespVO.class));
    }

}