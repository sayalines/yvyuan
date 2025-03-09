package cn.iocoder.yudao.module.member.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.*;
import cn.iocoder.yudao.module.member.convert.user.MemberUserConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.group.MemberGroupDO;
import cn.iocoder.yudao.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.yudao.module.member.dal.dataobject.tag.MemberTagDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.enums.point.MemberPointBizTypeEnum;
import cn.iocoder.yudao.module.member.service.group.MemberGroupService;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.point.MemberPointRecordService;
import cn.iocoder.yudao.module.member.service.tag.MemberTagService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 会员用户")
@RestController
@RequestMapping("/member/user")
@Validated
public class MemberUserController {

    @Resource
    private MemberUserService memberUserService;
    @Resource
    private MemberTagService memberTagService;
    @Resource
    private MemberLevelService memberLevelService;
    @Resource
    private MemberGroupService memberGroupService;
    @Resource
    private MemberPointRecordService memberPointRecordService;
    @Resource
    private DeptService deptService;

    @PutMapping("/update")
    @Operation(summary = "更新会员用户")
    @PreAuthorize("@ss.hasPermission('member:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody MemberUserUpdateReqVO updateReqVO) {
        memberUserService.updateUser(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-level")
    @Operation(summary = "更新会员用户等级")
    @PreAuthorize("@ss.hasPermission('member:user:update-level')")
    public CommonResult<Boolean> updateUserLevel(@Valid @RequestBody MemberUserUpdateLevelReqVO updateReqVO) {
        memberLevelService.updateUserLevel(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-point")
    @Operation(summary = "更新会员用户积分")
    @PreAuthorize("@ss.hasPermission('member:user:update-point')")
    public CommonResult<Boolean> updateUserPoint(@Valid @RequestBody MemberUserUpdatePointReqVO updateReqVO) {
        memberPointRecordService.createPointRecord(updateReqVO.getId(), updateReqVO.getPoint(),
                MemberPointBizTypeEnum.ADMIN, String.valueOf(getLoginUserId()));
        return success(true);
    }

    @PutMapping("/update-balance")
    @Operation(summary = "更新会员用户余额")
    @PreAuthorize("@ss.hasPermission('member:user:update-balance')")
    public CommonResult<Boolean> updateUserBalance(@Valid @RequestBody Long id) {
        // todo @jason：增加一个【修改余额】
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<MemberUserRespVO> getUser(@RequestParam("id") Long id) {
        MemberUserDO user = memberUserService.getUser(id);
        return success(MemberUserConvert.INSTANCE.convert100(user));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员用户分页")
    @PreAuthorize("@ss.hasPermission('member:user:query')")
    public CommonResult<PageResult<MemberUserRespVO>> getUserPage(@Valid MemberUserPageReqVO pageVO) {
        PageResult<MemberUserDO> pageResult = memberUserService.getUserPage(pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 处理用户标签返显
        Set<Long> tagIds = pageResult.getList().stream()
                .map(MemberUserDO::getTagIds)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        List<MemberTagDO> tags = memberTagService.getTagList(tagIds);
        // 处理用户级别返显
        List<MemberLevelDO> levels = memberLevelService.getLevelList(
                convertSet(pageResult.getList(), MemberUserDO::getLevelId));
        // 处理用户分组返显
        List<MemberGroupDO> groups = memberGroupService.getGroupList(
                convertSet(pageResult.getList(), MemberUserDO::getGroupId));
        // 处理部门返显
        Set<Long> deptIds = pageResult.getList().stream()
                .map(MemberUserDO::getDeptId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<DeptDO> depts = deptService.getDeptList(deptIds);
        Map<Long, String> deptMap = depts.stream()
                .collect(Collectors.toMap(DeptDO::getId, DeptDO::getName));

        PageResult<MemberUserRespVO> pageResultWithDept = MemberUserConvert.INSTANCE.convertPage(pageResult, tags, levels, groups, deptMap);
        return success(pageResultWithDept);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员记录 Excel")
    @PreAuthorize("@ss.hasPermission('member:user:export')")
    @OperateLog(type = EXPORT)
    public void exportExcel(@Valid MemberUserPageReqVO pageVO, HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<MemberUserDO> pageResult = memberUserService.getUserPage(pageVO);
        List<MemberUserExcelRespVO> list = new ArrayList<>();
        if (pageResult!=null && pageResult.getList()!=null && pageResult.getList().size()>0){
//            // 处理用户级别返显
//            List<MemberLevelDO> levels = memberLevelService.getLevelList(
//                    convertSet(pageResult.getList(), MemberUserDO::getLevelId));
//            if (levels==null){
//                levels = new ArrayList<>();
//            }
//            Map<Long, String> levelMap = convertMap(levels, MemberLevelDO::getId, MemberLevelDO::getName);


//            // 处理用户分组返显
//            List<MemberGroupDO> groups = memberGroupService.getGroupList(
//                    convertSet(pageResult.getList(), MemberUserDO::getGroupId));
//            if (groups==null){
//                groups = new ArrayList<>();
//            }
//            Map<Long, String> groupMap = convertMap(groups, MemberGroupDO::getId, MemberGroupDO::getName);

//            //openid
//            Set<Long> idsSet = convertSet(pageResult.getList(), MemberUserDO::getId);
//            List<MemberUserInfoVO> userList = memberUserService.getUserInfoList(new ArrayList<>(idsSet));
//            Map<Long, MemberUserInfoVO> userMap = convertMap(userList, MemberUserInfoVO::getId);

            // 处理部门返显
            Set<Long> deptIds = convertSet(pageResult.getList(), MemberUserDO::getDeptId);
            List<DeptDO> depts = deptService.getDeptList(deptIds);
            Map<Long, DeptDO> deptMap = convertMap(depts, DeptDO::getId);

            for(MemberUserDO dd:pageResult.getList()){
                MemberUserExcelRespVO dto = BeanUtils.toBean(dd,MemberUserExcelRespVO.class);
                dto.setCreateTime(formatLocalDateTime(dd.getCreateTime()));
                dto.setLoginDate(formatLocalDateTime(dd.getLoginDate()));
                dto.setSex(formatSex(dd.getSex()));
                if (dd.getAreaId()!=null){
                    dto.setAreaName(AreaUtils.format(dd.getAreaId()));
                }
                if (dd.getDeptId()!=null && deptMap.containsKey(dd.getDeptId())){
                    DeptDO deptDO = deptMap.get(dd.getDeptId());
                    if (deptDO!=null){
                        dto.setDeptName(deptDO.getName());
                    }
                }


//                dto.setIsTakeNewGiftPack(formatBoolean(dd.getIsTakeNewGiftPack()));
//                if (dd.getLevelId()!=null){
//                    if (levelMap.containsKey(dd.getLevelId())){
//                        dto.setLevelName(levelMap.get(dd.getLevelId()));
//                    }
//                }
//
//                if (dd.getGroupId()!=null){
//                    if (groupMap.containsKey(dd.getGroupId())){
//                        dto.setGroupName(levelMap.get(dd.getGroupId()));
//                    }
//                }
//
//                if (userMap.containsKey(dd.getId())){
//                    MemberUserInfoVO userInfoVO = userMap.get(dd.getId());
//                    dto.setOpenid(userInfoVO.getOpenid());
//                    dto.setUnionid(userInfoVO.getUnionid());
//                }
                list.add(dto);
            }
        }
        // 导出 Excel
        ExcelUtils.write(response, "会员记录.xls", "数据", MemberUserExcelRespVO.class,list);
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public String formatLocalDateTime(LocalDateTime dateTime){
        String result = "";
        if (dateTime!=null){
            String formatter = "yyyy-MM-dd HH:mm:ss";
            result = dateTime.format(DateTimeFormatter.ofPattern(formatter));
        }
        return result;
    }

    /**
     * 格式化性别
     * @param sex
     * @return
     */
    public String formatSex(Integer sex){
        String result = "未知";
        if(sex!=null){
            if (sex.compareTo(1)==0){
                result = "男";
            }else if (sex.compareTo(2)==0) {
                result = "女";
            }
        }
        return result;
    }


    /**
     * 格式化布尔值
     * @param value
     * @return
     */
    public String formatBoolean(Boolean value){
        String result = "";
        if(value!=null){
            if (value){
                result = "是";
            }else {
                result = "否";
            }
        }
        return result;
    }

}
