package cn.iocoder.yudao.server.convert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.notice.NoticeDO;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.vo.ApiNoticeDO;

import java.util.ArrayList;
import java.util.List;
public class RestNoticeConvert {
    public static PageResult<ApiNoticeDO> convert(PageResult<NoticeDO> page){
        PageResult<ApiNoticeDO> resultPage = new PageResult();
        resultPage.setTotal(page.getTotal());
        List<ApiNoticeDO> list = new ArrayList<>();
        if (page!=null && page.getList().size()>0){
            for(NoticeDO dd:page.getList()){
                list.add(convert(dd));
            }
        }
        resultPage.setList(list);
        return resultPage;
    }

    public static ApiNoticeDO convert(NoticeDO data){
        ApiNoticeDO result = BeanUtils.toBean(data,ApiNoticeDO.class);
        result.setCreateTime(CommonUtils.formatLocalDateTime(data.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        return result;
    }
}
