package cn.iocoder.yudao.module.promotion.controller.admin.article;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.promotion.convert.article.ArticleCategoryConvert;
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

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.*;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import cn.iocoder.yudao.module.promotion.service.article.ArticleCategoryService;

@Tag(name = "管理后台 - 文章分类")
@RestController
@RequestMapping("/promotion/article-category")
@Validated
public class ArticleCategoryController {

    @Resource
    private ArticleCategoryService articleCategoryService;

    @PostMapping("/create")
    @Operation(summary = "创建文章分类")
    @PreAuthorize("@ss.hasPermission('promotion:article-category:create')")
    public CommonResult<Long> createArticleCategory(@Valid @RequestBody ArticleCategorySaveReqVO createReqVO) {
        return success(articleCategoryService.createArticleCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文章分类")
    @PreAuthorize("@ss.hasPermission('promotion:article-category:update')")
    public CommonResult<Boolean> updateArticleCategory(@Valid @RequestBody ArticleCategorySaveReqVO updateReqVO) {
        articleCategoryService.updateArticleCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文章分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:article-category:delete')")
    public CommonResult<Boolean> deleteArticleCategory(@RequestParam("id") Long id) {
        articleCategoryService.deleteArticleCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文章分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('promotion:article-category:query')")
    public CommonResult<ArticleCategoryRespVO> getArticleCategory(@RequestParam("id") Long id) {
        ArticleCategoryDO articleCategory = articleCategoryService.getArticleCategory(id);
        return success(BeanUtils.toBean(articleCategory, ArticleCategoryRespVO.class));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取文章分类精简信息列表", description = "只包含被开启的文章分类，主要用于前端的下拉选项")
    public CommonResult<List<ArticleCategorySimpleRespVO>> getSimpleDeptList() {
        // 获得分类列表，只要开启状态的
        List<ArticleCategoryDO> list = articleCategoryService.getArticleCategoryListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 降序排序后，返回给前端
        list.sort(Comparator.comparing(ArticleCategoryDO::getSort).reversed());
        return success(BeanUtils.toBean(list, ArticleCategorySimpleRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得文章分类列表")
    @PreAuthorize("@ss.hasPermission('promotion:article-category:query')")
    public CommonResult<List<ArticleCategoryRespVO>> getArticleCategoryList(@Valid ArticleCategoryListReqVO listReqVO) {
        List<ArticleCategoryDO> list = articleCategoryService.getArticleCategoryList(listReqVO);
        return success(ArticleCategoryConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出文章分类 Excel")
    @PreAuthorize("@ss.hasPermission('promotion:article-category:export')")
    @OperateLog(type = EXPORT)
    public void exportArticleCategoryExcel(@Valid ArticleCategoryListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<ArticleCategoryDO> list = articleCategoryService.getArticleCategoryList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "文章分类.xls", "数据", ArticleCategoryRespVO.class,
                        BeanUtils.toBean(list, ArticleCategoryRespVO.class));
    }

}