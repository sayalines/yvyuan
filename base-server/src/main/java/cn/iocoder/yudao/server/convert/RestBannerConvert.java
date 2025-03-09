package cn.iocoder.yudao.server.convert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.promotion.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.server.vo.ApiBannerDO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestBannerConvert {
    public static PageResult<ApiBannerDO> convert(PageResult<BannerDO> page){
        PageResult<ApiBannerDO> resultPage = new PageResult();
        resultPage.setTotal(page.getTotal());
        List<ApiBannerDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(BannerDO dd:page.getList()){
                list.add(convert(dd));
            }
        }
        resultPage.setList(list);
        return resultPage;
    }

    public static ApiBannerDO convert(BannerDO data){
        ApiBannerDO result = BeanUtils.toBean(data,ApiBannerDO.class);
        return result;
    }

    public static List<ApiBannerDO> convertToList(List<BannerDO> bannerDOList) {
        return bannerDOList.stream()
                .map(RestBannerConvert::convert)
                .collect(Collectors.toList());
    }
}
