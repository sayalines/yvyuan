package cn.iocoder.yudao.module.member.controller.admin.memberactionlog;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo.MemberVisitLogRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
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

import cn.iocoder.yudao.module.member.controller.admin.memberactionlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.memberactionlog.MemberActionLogDO;
import cn.iocoder.yudao.module.member.service.memberactionlog.MemberActionLogService;

@Tag(name = "管理后台 - 访客行为日志")
@RestController
@RequestMapping("/member/action-log")
@Validated
public class MemberActionLogController {

    @Resource
    private MemberActionLogService actionLogService;

    @Resource
    private MemberUserService memberUserService;


    @PostMapping("/create")
    @Operation(summary = "创建访客行为日志")
    @PreAuthorize("@ss.hasPermission('member:action-log:create')")
    public CommonResult<Long> createActionLog(@Valid @RequestBody MemberActionLogSaveReqVO createReqVO) {
        return success(actionLogService.createActionLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新访客行为日志")
    @PreAuthorize("@ss.hasPermission('member:action-log:update')")
    public CommonResult<Boolean> updateActionLog(@Valid @RequestBody MemberActionLogSaveReqVO updateReqVO) {
        actionLogService.updateActionLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除访客行为日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:action-log:delete')")
    public CommonResult<Boolean> deleteActionLog(@RequestParam("id") Long id) {
        actionLogService.deleteActionLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得访客行为日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:action-log:query')")
    public CommonResult<MemberActionLogRespVO> getActionLog(@RequestParam("id") Long id) {
        MemberActionLogDO actionLog = actionLogService.getActionLog(id);
        return success(BeanUtils.toBean(actionLog, MemberActionLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得访客行为日志分页")
    @PreAuthorize("@ss.hasPermission('member:action-log:query')")
    public CommonResult<PageResult<MemberActionLogRespVO>> getActionLogPage(@Valid MemberActionLogPageReqVO pageReqVO) {
        PageResult<MemberActionLogDO> pageResult = actionLogService.getActionLogPage(pageReqVO);
        PageResult<MemberActionLogRespVO> page = BeanUtils.toBean(pageResult, MemberActionLogRespVO.class);
        if (page!=null && page.getList()!=null && page.getList().size()>0) {
            Set<Long> userIds = CollectionUtils.convertSet(page.getList(), MemberActionLogRespVO::getUserId);
            if (userIds != null && userIds.size() > 0) {
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList != null && userList.size() > 0) {
                    Map<Long,MemberUserDO> userMap = CollectionUtils.convertMap(userList,MemberUserDO::getId);
                    for(MemberActionLogRespVO dd:page.getList()) {
                        if (dd.getUserId() != null && userMap.containsKey(dd.getUserId())) {
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setNickname(userDO.getNickname());
                        }
                    }
                }
            }
        }
        return success(page);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出访客行为日志 Excel")
    @PreAuthorize("@ss.hasPermission('member:action-log:export')")
    @OperateLog(type = EXPORT)
    public void exportActionLogExcel(@Valid MemberActionLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MemberActionLogDO> list = actionLogService.getActionLogPage(pageReqVO).getList();
        List<MemberActionLogRespVO> resList = BeanUtils.toBean(list, MemberActionLogRespVO.class);
        if (resList!=null && resList.size()>0) {
            Set<Long> userIds = CollectionUtils.convertSet(resList, MemberActionLogRespVO::getUserId);
            if (userIds != null && userIds.size() > 0) {
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList != null && userList.size() > 0) {
                    Map<Long, MemberUserDO> userMap = CollectionUtils.convertMap(userList, MemberUserDO::getId);
                    for (MemberActionLogRespVO dd : resList) {
                        if (dd.getUserId() != null && userMap.containsKey(dd.getUserId())) {
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setNickname(userDO.getNickname());
                        }
                    }
                }
            }
        }
        // 导出 Excel
        ExcelUtils.write(response, "访客行为日志.xls", "数据", MemberActionLogRespVO.class, resList);
    }

}