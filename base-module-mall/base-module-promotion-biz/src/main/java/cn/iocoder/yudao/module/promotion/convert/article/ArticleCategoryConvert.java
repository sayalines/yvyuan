package cn.iocoder.yudao.module.promotion.convert.article;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.category.*;
import cn.iocoder.yudao.module.promotion.controller.app.article.vo.category.AppArticleCategoryRespVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 文章分类 Convert
 *
 * @author HUIHUI
 */
@Mapper(componentModel = "spring")
public interface ArticleCategoryConvert {

    ArticleCategoryConvert INSTANCE = Mappers.getMapper(ArticleCategoryConvert.class);

    ArticleCategoryDO convert(ArticleCategoryCreateReqVO bean);

    ArticleCategoryDO convert(ArticleCategoryUpdateReqVO bean);

    ArticleCategoryRespVO convert(ArticleCategoryDO bean);

    PageResult<ArticleCategoryRespVO> convertPage(PageResult<ArticleCategoryDO> page);

    List<ArticleCategorySimpleRespVO> convertList03(List<ArticleCategoryDO> list);

    List<AppArticleCategoryRespVO> convertList04(List<ArticleCategoryDO> categoryList);

    default List<ArticleCategoryRespVO> convertList(List<ArticleCategoryDO> list){
        List<ArticleCategoryRespVO> result = BeanUtils.toBean(list, ArticleCategoryRespVO.class);
        Map<Long, String> map = convertMap(list, ArticleCategoryDO::getId, ArticleCategoryDO::getName);
        map.put(0L, "顶级文章分类");
        result.forEach(vo -> {
            vo.setParentName(map.get(vo.getParentId()));
        });
        return result;
    }

}
