package cn.iocoder.yudao.module.promotion.controller.admin.article;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleUpdateReqVO;
import cn.iocoder.yudao.module.promotion.convert.article.ArticleConvert;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import cn.iocoder.yudao.module.promotion.service.article.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 文章管理")
@RestController
@RequestMapping("/promotion/article")
@Validated
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/create")
    @Operation(summary = "创建文章管理")
    @PreAuthorize("@ss.hasPermission('promotion:article:create')")
    public CommonResult<Long> createArticle(@Valid @RequestBody ArticleCreateReqVO createReqVO) {
        return success(articleService.createArticle(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文章管理")
    @PreAuthorize("@ss.hasPermission('promotion:article:update')")
    public CommonResult<Boolean> updateArticle(@Valid @RequestBody ArticleUpdateReqVO updateReqVO) {
        articleService.updateArticle(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文章管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:article:delete')")
    public CommonResult<Boolean> deleteArticle(@RequestParam("id") Long id) {
        articleService.deleteArticle(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文章管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('promotion:article:query')")
    public CommonResult<ArticleRespVO> getArticle(@RequestParam("id") Long id) {
        ArticleDO article = articleService.getArticle(id);
        return success(ArticleConvert.INSTANCE.convert(article));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文章管理分页")
    @PreAuthorize("@ss.hasPermission('promotion:article:query')")
    public CommonResult<PageResult<ArticleRespVO>> getArticlePage(@Valid ArticlePageReqVO pageVO) {
        PageResult<ArticleDO> pageResult = articleService.getArticlePage(pageVO);
        return success(ArticleConvert.INSTANCE.convertPage(pageResult));
    }

    @PutMapping("/createLinkEq")
    @Operation(summary = "生成设备介绍链接")
    @PreAuthorize("@ss.hasPermission('promotion:article:create')")
    public CommonResult<Boolean> createLinkEq(@RequestParam("id") Long id) {
        articleService.createLinkEq(id);
        return success(true);
    }

    @PutMapping("/createLinkEx")
    @Operation(summary = "生成拓展资料链接")
    @PreAuthorize("@ss.hasPermission('promotion:article:create')")
    public CommonResult<Boolean> createLinkEx(@RequestParam("id") Long id) {
        articleService.createLinkEx(id);
        return success(true);
    }

    @PutMapping("/createLinkIn")
    @Operation(summary = "生成资讯链接")
    @PreAuthorize("@ss.hasPermission('promotion:article:create')")
    public CommonResult<Boolean> createLinkIn(@RequestParam("id") Long id) {
        articleService.createLinkIn(id);
        return success(true);
    }

    @PutMapping("/createLinkPo")
    @Operation(summary = "生成科普知识链接")
    @PreAuthorize("@ss.hasPermission('promotion:article:create')")
    public CommonResult<Boolean> createLinkPo(@RequestParam("id") Long id) {
        articleService.createLinkPo(id);
        return success(true);
    }
}
