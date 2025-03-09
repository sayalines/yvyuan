package cn.iocoder.yudao.server.convert;


import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.server.vo.*;
import java.util.ArrayList;
import java.util.List;


public class RestDictDataConvert {

    public static List<ApiDictDataDO> convert(List<DictDataDO> list){
        List<ApiDictDataDO> resultList = new ArrayList<>();
        if (list!=null){
            for(DictDataDO dd:list){
                ApiDictDataDO dto = new ApiDictDataDO();
                dto.setLabel(dd.getLabel());
                dto.setValue(dd.getValue());
                resultList.add(dto);
            }
        }
        return resultList;
    }
}
