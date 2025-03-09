package cn.iocoder.yudao.module.member.controller.admin.membervisitlog;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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

import cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.membervisitlog.MemberVisitLogDO;
import cn.iocoder.yudao.module.member.service.membervisitlog.MemberVisitLogService;

@Tag(name = "管理后台 - 访客信息日志")
@RestController
@RequestMapping("/member/visit-log")
@Validated
public class MemberVisitLogController {

    @Resource
    private MemberVisitLogService visitLogService;
    @Resource
    private MemberUserService memberUserService;

    @PostMapping("/create")
    @Operation(summary = "创建访客信息日志")
    @PreAuthorize("@ss.hasPermission('member:visit-log:create')")
    public CommonResult<Long> createVisitLog(@Valid @RequestBody MemberVisitLogSaveReqVO createReqVO) {
        return success(visitLogService.createVisitLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新访客信息日志")
    @PreAuthorize("@ss.hasPermission('member:visit-log:update')")
    public CommonResult<Boolean> updateVisitLog(@Valid @RequestBody MemberVisitLogSaveReqVO updateReqVO) {
        visitLogService.updateVisitLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除访客信息日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:visit-log:delete')")
    public CommonResult<Boolean> deleteVisitLog(@RequestParam("id") Long id) {
        visitLogService.deleteVisitLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得访客信息日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:visit-log:query')")
    public CommonResult<MemberVisitLogRespVO> getVisitLog(@RequestParam("id") Long id) {
        MemberVisitLogDO visitLog = visitLogService.getVisitLog(id);
        return success(BeanUtils.toBean(visitLog, MemberVisitLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得访客信息日志分页")
    @PreAuthorize("@ss.hasPermission('member:visit-log:query')")
    public CommonResult<PageResult<MemberVisitLogRespVO>> getVisitLogPage(@Valid MemberVisitLogPageReqVO pageReqVO) {
        PageResult<MemberVisitLogDO> pageResult = visitLogService.getVisitLogPage(pageReqVO);
        PageResult<MemberVisitLogRespVO> page = BeanUtils.toBean(pageResult, MemberVisitLogRespVO.class);
        if (page!=null && page.getList()!=null && page.getList().size()>0){
            Set<Long> userIds = CollectionUtils.convertSet(page.getList(),MemberVisitLogRespVO::getUserId);
            if (userIds!=null && userIds.size()>0){
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList!=null && userList.size()>0){
                    Map<Long,MemberUserDO> userMap = CollectionUtils.convertMap(userList,MemberUserDO::getId);
                    for(MemberVisitLogRespVO dd:page.getList()){
                        if (dd.getUserId()!=null && userMap.containsKey(dd.getUserId())){
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setMobile(userDO.getMobile());
                            dd.setAvatar(userDO.getAvatar());
                            dd.setBirthday(userDO.getBirthday());
                            dd.setNickname(userDO.getNickname());
                            if (userDO.getSex()!=null){
                                if (userDO.getSex().equals(1)){
                                    dd.setSex("男");
                                }else if (userDO.getSex().equals(2)){
                                    dd.setSex("女");
                                }else if (userDO.getSex().equals(3)){
                                    dd.setSex("未知");
                                }
                            }
                            if (userDO.getBirthday()!=null){
                                dd.setAge(Period.between(userDO.getBirthday().toLocalDate(), LocalDate.now()).getYears());
                            }
                        }
                    }
                }
            }
        }
        return success(page);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出访客信息日志 Excel")
    @PreAuthorize("@ss.hasPermission('member:visit-log:export')")
    @OperateLog(type = EXPORT)
    public void exportVisitLogExcel(@Valid MemberVisitLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MemberVisitLogDO> list = visitLogService.getVisitLogPage(pageReqVO).getList();
        // 导出 Excel
        List<MemberVisitLogRespVO> resList =  BeanUtils.toBean(list, MemberVisitLogRespVO.class);
        if (resList!=null && resList.size()>0){
            Set<Long> userIds = CollectionUtils.convertSet(resList,MemberVisitLogRespVO::getUserId);
            if (userIds!=null && userIds.size()>0){
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList!=null && userList.size()>0){
                    Map<Long,MemberUserDO> userMap = CollectionUtils.convertMap(userList,MemberUserDO::getId);
                    for(MemberVisitLogRespVO dd:resList){
                        if (dd.getUserId()!=null && userMap.containsKey(dd.getUserId())){
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setMobile(userDO.getMobile());
                            dd.setAvatar(userDO.getAvatar());
                            dd.setBirthday(userDO.getBirthday());
                            dd.setNickname(userDO.getNickname());
                            if (userDO.getSex()!=null){
                                if (userDO.getSex().equals(1)){
                                    dd.setSex("男");
                                }else if (userDO.getSex().equals(2)){
                                    dd.setSex("女");
                                }else if (userDO.getSex().equals(3)){
                                    dd.setSex("未知");
                                }
                            }
                            if (userDO.getBirthday()!=null){
                                dd.setAge(Period.between(userDO.getBirthday().toLocalDate(), LocalDate.now()).getYears());
                            }
                        }
                    }
                }
            }
        }
        ExcelUtils.write(response, "访客信息日志.xls", "数据", MemberVisitLogRespVO.class, resList);
    }

}