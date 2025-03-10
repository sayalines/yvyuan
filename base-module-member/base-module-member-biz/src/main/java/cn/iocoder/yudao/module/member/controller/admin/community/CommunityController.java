package cn.iocoder.yudao.module.member.controller.admin.community;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.member.controller.admin.community.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.community.CommunityDO;
import cn.iocoder.yudao.module.member.service.community.CommunityService;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.product.dal.dataobject.brand.ProductBrandDO;
import cn.iocoder.yudao.module.product.service.brand.ProductBrandService;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 论坛文章")
@RestController
@RequestMapping("/community/community")
@Validated
public class CommunityController {

    @Resource
    private CommunityService communityService;
    @Resource
    private ProductBrandService productBrandService;
    @Resource
    private MemberUserService memberUserService;

    @PostMapping("/create")
    @Operation(summary = "创建论坛文章")
    @PreAuthorize("@ss.hasPermission('community:community:create')")
    public CommonResult<Long> createCommunity(@Valid @RequestBody CommunitySaveReqVO createReqVO) {
        return success(communityService.createCommunity(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新论坛文章")
    @PreAuthorize("@ss.hasPermission('community:community:update')")
    public CommonResult<Boolean> updateCommunity(@Valid @RequestBody CommunitySaveReqVO updateReqVO) {
        communityService.updateCommunity(updateReqVO);
        return success(true);
    }

    @PutMapping("/audit")
    @Operation(summary = "审核论坛文章")
    @PreAuthorize("@ss.hasPermission('community:community:update')")
    public CommonResult<Boolean> auditCommunity(Long id, Integer status, String reason) {
        CommunityAuditReqVO auditReqVO = new CommunityAuditReqVO();
        auditReqVO.setId(id);
        auditReqVO.setStatus(status);
        auditReqVO.setReason(reason);
        communityService.auditCommunity(auditReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除论坛文章")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('community:community:delete')")
    public CommonResult<Boolean> deleteCommunity(@RequestParam("id") Long id) {
        communityService.deleteCommunity(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得论坛文章")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('community:community:query')")
    public CommonResult<CommunityRespVO> getCommunity(@RequestParam("id") Long id) {
        CommunityDO community = communityService.getCommunity(id);
        CommunityRespVO respVO = BeanUtils.toBean(community, CommunityRespVO.class);
        if (respVO.getUserId()!=null){
            MemberUserDO userDO = memberUserService.getUser(respVO.getUserId());
            if (userDO!=null){
                respVO.setNickname(userDO.getNickname());
                respVO.setMobile(userDO.getMobile());
            }
        }
        if (respVO.getBrandId()!=null){
            ProductBrandDO brandDO = productBrandService.getBrand(respVO.getBrandId());
            if (brandDO!=null){
                respVO.setBrand(brandDO.getName());
            }
        }
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得论坛文章分页")
    @PreAuthorize("@ss.hasPermission('community:community:query')")
    public CommonResult<PageResult<CommunityRespVO>> getCommunityPage(@Valid CommunityPageReqVO pageReqVO) {
        PageResult<CommunityDO> pageResult = communityService.getCommunityPage(pageReqVO);
        PageResult<CommunityRespVO> page = BeanUtils.toBean(pageResult, CommunityRespVO.class);
        if (page!=null && page.getList()!=null && page.getList().size()>0) {
            Set<Long> userIds = CollectionUtils.convertSet(page.getList(), CommunityRespVO::getUserId);
            if (userIds != null && userIds.size() > 0) {
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList != null && userList.size() > 0) {
                    Map<Long, MemberUserDO> userMap = CollectionUtils.convertMap(userList, MemberUserDO::getId);
                    for (CommunityRespVO dd : page.getList()) {
                        if (dd.getUserId() != null && userMap.containsKey(dd.getUserId())) {
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setNickname(userDO.getNickname());
                            dd.setMobile(userDO.getMobile());
                        }
                    }
                }
            }

            Set<Long> brandIds = CollectionUtils.convertSet(page.getList(), CommunityRespVO::getBrandId);
            if (brandIds != null && brandIds.size() > 0) {
                List<ProductBrandDO> brandList = productBrandService.getBrandList(brandIds);
                if (brandList != null && brandList.size() > 0) {
                    Map<Long, ProductBrandDO> brandMap = CollectionUtils.convertMap(brandList, ProductBrandDO::getId);
                    for (CommunityRespVO dd : page.getList()) {
                        if (dd.getBrandId() != null && brandMap.containsKey(dd.getBrandId())) {
                            ProductBrandDO brandDO = brandMap.get(dd.getBrandId());
                            dd.setBrand(brandDO.getName());
                        }
                    }
                }
            }
        }
        return success(page);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出论坛文章 Excel")
    @PreAuthorize("@ss.hasPermission('community:community:export')")
    @OperateLog(type = EXPORT)
    public void exportCommunityExcel(@Valid CommunityPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CommunityDO> list = communityService.getCommunityPage(pageReqVO).getList();
        List<CommunityRespVO> resList = BeanUtils.toBean(list, CommunityRespVO.class);
        if (resList!=null && resList.size()>0) {
            Set<Long> userIds = CollectionUtils.convertSet(resList, CommunityRespVO::getUserId);
            if (userIds != null && userIds.size() > 0) {
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList != null && userList.size() > 0) {
                    Map<Long, MemberUserDO> userMap = CollectionUtils.convertMap(userList, MemberUserDO::getId);
                    for (CommunityRespVO dd : resList) {
                        if (dd.getUserId() != null && userMap.containsKey(dd.getUserId())) {
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setNickname(userDO.getNickname());
                            dd.setMobile(userDO.getMobile());
                        }
                    }
                }
            }

            Set<Long> brandIds = CollectionUtils.convertSet(resList, CommunityRespVO::getBrandId);
            if (brandIds != null && brandIds.size() > 0) {
                List<ProductBrandDO> brandList = productBrandService.getBrandList(brandIds);
                if (brandList != null && brandList.size() > 0) {
                    Map<Long, ProductBrandDO> brandMap = CollectionUtils.convertMap(brandList, ProductBrandDO::getId);
                    for (CommunityRespVO dd : resList) {
                        if (dd.getBrandId() != null && brandMap.containsKey(dd.getBrandId())) {
                            ProductBrandDO brandDO = brandMap.get(dd.getBrandId());
                            dd.setBrand(brandDO.getName());
                        }
                    }
                }
            }
        }
        // 导出 Excel
        ExcelUtils.write(response, "论坛文章.xls", "数据", CommunityRespVO.class,
                resList);
    }

}