package cn.iocoder.yudao.module.member.controller.admin.messageremind;

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

import cn.iocoder.yudao.module.member.controller.admin.messageremind.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.messageremind.MessageRemindDO;
import cn.iocoder.yudao.module.member.service.messageremind.MessageRemindService;

@Tag(name = "管理后台 - 会员消息提醒")
@RestController
@RequestMapping("/member/message-remind")
@Validated
public class MessageRemindController {

    @Resource
    private MessageRemindService messageRemindService;

    @PostMapping("/create")
    @Operation(summary = "创建会员消息提醒")
    @PreAuthorize("@ss.hasPermission('member:message-remind:create')")
    public CommonResult<Long> createMessageRemind(@Valid @RequestBody MessageRemindSaveReqVO createReqVO) {
        return success(messageRemindService.createMessageRemind(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员消息提醒")
    @PreAuthorize("@ss.hasPermission('member:message-remind:update')")
    public CommonResult<Boolean> updateMessageRemind(@Valid @RequestBody MessageRemindSaveReqVO updateReqVO) {
        messageRemindService.updateMessageRemind(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员消息提醒")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:message-remind:delete')")
    public CommonResult<Boolean> deleteMessageRemind(@RequestParam("id") Long id) {
        messageRemindService.deleteMessageRemind(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员消息提醒")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:message-remind:query')")
    public CommonResult<MessageRemindRespVO> getMessageRemind(@RequestParam("id") Long id) {
        MessageRemindDO messageRemind = messageRemindService.getMessageRemind(id);
        return success(BeanUtils.toBean(messageRemind, MessageRemindRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员消息提醒分页")
    @PreAuthorize("@ss.hasPermission('member:message-remind:query')")
    public CommonResult<PageResult<MessageRemindRespVO>> getMessageRemindPage(@Valid MessageRemindPageReqVO pageReqVO) {
        PageResult<MessageRemindDO> pageResult = messageRemindService.getMessageRemindPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MessageRemindRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员消息提醒 Excel")
    @PreAuthorize("@ss.hasPermission('member:message-remind:export')")
    @OperateLog(type = EXPORT)
    public void exportMessageRemindExcel(@Valid MessageRemindPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MessageRemindDO> list = messageRemindService.getMessageRemindPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员消息提醒.xls", "数据", MessageRemindRespVO.class,
                        BeanUtils.toBean(list, MessageRemindRespVO.class));
    }

}