package cn.iocoder.yudao.server.convert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.vo.ApiArticleDO;
import cn.iocoder.yudao.server.vo.ApiDeptDO;

import java.util.ArrayList;
import java.util.List;

public class RestDeptConvert {

    public static PageResult<ApiDeptDO> convert(PageResult<DeptDO> page){
        PageResult<ApiDeptDO> resultPage = new PageResult();
        resultPage.setTotal(page.getTotal());
        List<ApiDeptDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(DeptDO dd:page.getList()){
                list.add(convert(dd));
            }
        }
        resultPage.setList(list);
        return resultPage;
    }

    public static ApiDeptDO convert(DeptDO data){
        if (data==null){
            return null;
        }
        ApiDeptDO result = BeanUtils.toBean(data,ApiDeptDO.class);
        return result;
    }
}
