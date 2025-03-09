package cn.iocoder.yudao.module.member.controller.admin.articlevisit;

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

import cn.iocoder.yudao.module.member.controller.admin.articlevisit.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.articlevisit.ArticleVisitDO;
import cn.iocoder.yudao.module.member.service.articlevisit.ArticleVisitService;

@Tag(name = "管理后台 - 文章访问日志")
@RestController
@RequestMapping("/member/article-visit")
@Validated
public class ArticleVisitController {

    @Resource
    private ArticleVisitService articleVisitService;

    @PostMapping("/create")
    @Operation(summary = "创建文章访问日志")
    @PreAuthorize("@ss.hasPermission('member:article-visit:create')")
    public CommonResult<Long> createArticleVisit(@Valid @RequestBody ArticleVisitSaveReqVO createReqVO) {
        return success(articleVisitService.createArticleVisit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文章访问日志")
    @PreAuthorize("@ss.hasPermission('member:article-visit:update')")
    public CommonResult<Boolean> updateArticleVisit(@Valid @RequestBody ArticleVisitSaveReqVO updateReqVO) {
        articleVisitService.updateArticleVisit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文章访问日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:article-visit:delete')")
    public CommonResult<Boolean> deleteArticleVisit(@RequestParam("id") Long id) {
        articleVisitService.deleteArticleVisit(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文章访问日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:article-visit:query')")
    public CommonResult<ArticleVisitRespVO> getArticleVisit(@RequestParam("id") Long id) {
        ArticleVisitDO articleVisit = articleVisitService.getArticleVisit(id);
        return success(BeanUtils.toBean(articleVisit, ArticleVisitRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文章访问日志分页")
    @PreAuthorize("@ss.hasPermission('member:article-visit:query')")
    public CommonResult<PageResult<ArticleVisitRespVO>> getArticleVisitPage(@Valid ArticleVisitPageReqVO pageReqVO) {
        PageResult<ArticleVisitDO> pageResult = articleVisitService.getArticleVisitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ArticleVisitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出文章访问日志 Excel")
    @PreAuthorize("@ss.hasPermission('member:article-visit:export')")
    @OperateLog(type = EXPORT)
    public void exportArticleVisitExcel(@Valid ArticleVisitPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ArticleVisitDO> list = articleVisitService.getArticleVisitPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "文章访问日志.xls", "数据", ArticleVisitRespVO.class,
                        BeanUtils.toBean(list, ArticleVisitRespVO.class));
    }

}