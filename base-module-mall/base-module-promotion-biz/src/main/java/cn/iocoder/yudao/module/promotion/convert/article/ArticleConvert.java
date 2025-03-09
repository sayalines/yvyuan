package cn.iocoder.yudao.module.promotion.convert.article;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleCreateReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleRespVO;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticleUpdateReqVO;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.article.AppArticleRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章管理 Convert
 *
 * @author HUIHUI
 */
@Mapper
public interface ArticleConvert {

    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);

    default ArticleDO convert(ArticleCreateReqVO bean){
        if ( bean == null ) {
            return null;
        }

        ArticleDO.ArticleDOBuilder articleDO = ArticleDO.builder();

        articleDO.categoryId( bean.getCategoryId() );
//        articleDO.spuId( bean.getSpuId() );
        articleDO.title( bean.getTitle() );
        articleDO.author( bean.getAuthor() );
        articleDO.picUrl( bean.getPicUrl() );
        articleDO.qrcodeUrl( bean.getQrcodeUrl());
        articleDO.introduction( bean.getIntroduction() );
        articleDO.sort( bean.getSort() );
        articleDO.status( bean.getStatus() );
        articleDO.ifExhibition( bean.getIfExhibition() );
//        articleDO.recommendHot( bean.getRecommendHot() );
//        articleDO.recommendBanner( bean.getRecommendBanner() );
        articleDO.content( bean.getContent() );
        articleDO.publishDate(bean.getPublishDate());
        articleDO.tag(bean.getTag());
        List<String> list = bean.getPictures();
        if ( list != null ) {
            articleDO.pictures( new ArrayList<String>( list ) );
        }
        articleDO.href(bean.getHref());
        articleDO.files(bean.getFiles());
        articleDO.remark( bean.getRemark() );
        articleDO.attr01(bean.getAttr01());
        articleDO.attr02(bean.getAttr02());
        articleDO.hitCount(bean.getHitCount());
        articleDO.attr03(bean.getAttr03());
        articleDO.attr04(bean.getAttr04());
        articleDO.attr05(bean.getAttr05());

        return articleDO.build();
    };

    default ArticleDO convert(ArticleUpdateReqVO bean){
        if ( bean == null ) {
            return null;
        }

        ArticleDO.ArticleDOBuilder articleDO = ArticleDO.builder();

        articleDO.id( bean.getId() );
        articleDO.categoryId( bean.getCategoryId() );
//        articleDO.spuId( bean.getSpuId() );
        articleDO.title( bean.getTitle() );
        articleDO.author( bean.getAuthor() );
        articleDO.picUrl( bean.getPicUrl() );
        articleDO.qrcodeUrl( bean.getQrcodeUrl() );
        articleDO.introduction( bean.getIntroduction() );
        articleDO.sort( bean.getSort() );
        articleDO.status( bean.getStatus() );
        articleDO.ifExhibition( bean.getIfExhibition() );
//        articleDO.recommendHot( bean.getRecommendHot() );
//        articleDO.recommendBanner( bean.getRecommendBanner() );
        articleDO.content( bean.getContent() );
        articleDO.remark( bean.getRemark() );
        articleDO.publishDate(bean.getPublishDate());
        articleDO.tag(bean.getTag());
        List<String> list = bean.getPictures();
        if ( list != null ) {
            articleDO.pictures( new ArrayList<String>( list ) );
        }
        articleDO.href(bean.getHref());
        articleDO.files(bean.getFiles());
        articleDO.attr01(bean.getAttr01());
        articleDO.attr02(bean.getAttr02());
        articleDO.hitCount(bean.getHitCount());
        articleDO.attr03(bean.getAttr03());
        articleDO.attr04(bean.getAttr04());
        articleDO.attr05(bean.getAttr05());

        return articleDO.build();
    };

    default ArticleRespVO convert(ArticleDO bean){
        if ( bean == null ) {
            return null;
        }

        ArticleRespVO articleRespVO = new ArticleRespVO();

        articleRespVO.setCategoryId( bean.getCategoryId() );
//        articleRespVO.setSpuId( bean.getSpuId() );
        articleRespVO.setTitle( bean.getTitle() );
        articleRespVO.setAuthor( bean.getAuthor() );
        articleRespVO.setPicUrl( bean.getPicUrl() );
        articleRespVO.setQrcodeUrl( bean.getQrcodeUrl() );
        articleRespVO.setIntroduction( bean.getIntroduction() );
        articleRespVO.setSort( bean.getSort() );
        articleRespVO.setStatus( bean.getStatus() );
        articleRespVO.setIfExhibition( bean.getIfExhibition() );
//        articleRespVO.setRecommendHot( bean.getRecommendHot() );
//        articleRespVO.setRecommendBanner( bean.getRecommendBanner() );
        articleRespVO.setContent( bean.getContent() );
        articleRespVO.setId( bean.getId() );
        articleRespVO.setBrowseCount( bean.getBrowseCount() );
        articleRespVO.setCreateTime( bean.getCreateTime() );
        articleRespVO.setPublishDate(bean.getPublishDate());
        articleRespVO.setTag(bean.getTag());
        List<String> list = bean.getPictures();
        if ( list != null ) {
            articleRespVO.setPictures( new ArrayList<String>( list ) );
        }
        articleRespVO.setHref(bean.getHref());
        articleRespVO.setFiles(bean.getFiles());
        articleRespVO.setAttr01(bean.getAttr01());
        articleRespVO.setAttr02(bean.getAttr02());
        articleRespVO.setHitCount(bean.getHitCount());
        articleRespVO.setRemark(bean.getRemark());
        articleRespVO.setAttr03(bean.getAttr03());
        articleRespVO.setAttr04(bean.getAttr04());
        articleRespVO.setAttr05(bean.getAttr05());

        return articleRespVO;
    };

    default List<ArticleRespVO> convertList(List<ArticleDO> list){
        if ( list == null ) {
            return null;
        }

        List<ArticleRespVO> list1 = new ArrayList<ArticleRespVO>( list.size() );
        for ( ArticleDO articleDO : list ) {
            list1.add( convert( articleDO ) );
        }

        return list1;
    };

    default PageResult<ArticleRespVO> convertPage(PageResult<ArticleDO> page){
        if ( page == null ) {
            return null;
        }

        PageResult<ArticleRespVO> pageResult = new PageResult<ArticleRespVO>();

        pageResult.setList( convertList( page.getList() ) );
        pageResult.setTotal( page.getTotal() );

        return pageResult;
    };

    default AppArticleRespVO convert01(ArticleDO article){
        if ( article == null ) {
            return null;
        }

        AppArticleRespVO appArticleRespVO = new AppArticleRespVO();

        appArticleRespVO.setId( article.getId() );
        appArticleRespVO.setTitle( article.getTitle() );
        appArticleRespVO.setAuthor( article.getAuthor() );
        appArticleRespVO.setCategoryId( article.getCategoryId() );
        appArticleRespVO.setPicUrl( article.getPicUrl() );
        appArticleRespVO.setQrcodeUrl( article.getQrcodeUrl() );
        appArticleRespVO.setIntroduction( article.getIntroduction() );
        appArticleRespVO.setCreateTime( article.getCreateTime() );
        appArticleRespVO.setBrowseCount( article.getBrowseCount() );
//        appArticleRespVO.setSpuId( article.getSpuId() );
        appArticleRespVO.setPublishDate(article.getPublishDate());
        appArticleRespVO.setTag(article.getTag());
        List<String> list = article.getPictures();
        if ( list != null ) {
            appArticleRespVO.setPictures( new ArrayList<String>( list ) );
        }
        appArticleRespVO.setHref(article.getHref());
        appArticleRespVO.setFiles(article.getFiles());
        appArticleRespVO.setAttr01(article.getAttr01());
        appArticleRespVO.setAttr02(article.getAttr02());
        appArticleRespVO.setHitCount(article.getHitCount());
        appArticleRespVO.setRemark(article.getRemark());
        appArticleRespVO.setAttr03(article.getAttr03());
        appArticleRespVO.setAttr04(article.getAttr04());
        appArticleRespVO.setAttr05(article.getAttr05());

        return appArticleRespVO;
    };

    default PageResult<AppArticleRespVO> convertPage02(PageResult<ArticleDO> articlePage){
        if ( articlePage == null ) {
            return null;
        }

        PageResult<AppArticleRespVO> pageResult = new PageResult<AppArticleRespVO>();

        pageResult.setList( convertList03( articlePage.getList() ) );
        pageResult.setTotal( articlePage.getTotal() );

        return pageResult;
    };

    default List<AppArticleRespVO> convertList03(List<ArticleDO> articleCategoryListByRecommendHotAndRecommendBanner){
        if ( articleCategoryListByRecommendHotAndRecommendBanner == null ) {
            return null;
        }

        List<AppArticleRespVO> list = new ArrayList<AppArticleRespVO>( articleCategoryListByRecommendHotAndRecommendBanner.size() );
        for ( ArticleDO articleDO : articleCategoryListByRecommendHotAndRecommendBanner ) {
            list.add( convert01( articleDO ) );
        }

        return list;
    };

}
