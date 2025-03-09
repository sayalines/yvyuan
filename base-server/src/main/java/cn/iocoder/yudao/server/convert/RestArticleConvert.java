package cn.iocoder.yudao.server.convert;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.vo.ApiArticleCategoryDO;
import cn.iocoder.yudao.server.vo.ApiArticleDO;
import cn.iocoder.yudao.server.vo.ApiArticleGroupDO;

import java.util.ArrayList;
import java.util.List;


public class RestArticleConvert {

    public static List<ApiArticleCategoryDO> convert(List<ArticleCategoryDO> list){
        List<ApiArticleCategoryDO> resultList = new ArrayList<>();
        if (list!=null){
            for(ArticleCategoryDO dd:list){
                resultList.add(BeanUtils.toBean(dd,ApiArticleCategoryDO.class));
            }
        }
        return resultList;
    }

    public static PageResult<ApiArticleDO> convert(PageResult<ArticleDO> page){
        PageResult<ApiArticleDO> resultPage = new PageResult();
        resultPage.setTotal(page.getTotal());
        List<ApiArticleDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(ArticleDO dd:page.getList()){
                list.add(convert(dd));
            }
        }
        resultPage.setList(list);
        return resultPage;
    }

    public static ApiArticleDO convert(ArticleDO data){
        if (data==null){
            return null;
        }
        ApiArticleDO result = BeanUtils.toBean(data,ApiArticleDO.class);
        result.setPublishDate(CommonUtils.formatLocalDateTime(data.getPublishDate(),"yyyy-MM-dd HH:mm:ss"));
        return result;
    }

    public static List<ApiArticleGroupDO> convertGroup(List<ArticleDO> list){
        List<ApiArticleGroupDO> resultList = new ArrayList<>();
        if (list!=null && list.size()>0){
            List<ApiArticleDO> allList = new ArrayList<>();
            List<String> groupList = new ArrayList<>();
            for(ArticleDO dd:list){
                ApiArticleDO dto = convert(dd);
                String group = dto.getPublishDate();
                if (groupList.indexOf(group)==-1){
                    groupList.add(group);
                }
                allList.add(dto);
            }

            for(String group:groupList){
                ApiArticleGroupDO dto = new ApiArticleGroupDO();
                dto.setGroup(group);
                List<ApiArticleDO> list01 = new ArrayList<>();
                for(ApiArticleDO dd:allList){
                    if (dd.getPublishDate().equalsIgnoreCase(group)){
                        list01.add(dd);
                    }
                }
                dto.setList(list01);

                resultList.add(dto);
            }
        }
        return resultList;
    }
}
