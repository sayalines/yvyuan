package cn.iocoder.yudao.module.product.service.property;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.controller.admin.property.vo.property.*;
import cn.iocoder.yudao.module.product.dal.dataobject.property.ProductPropertyDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 商品属性项 Service 接口
 *
 * @author 商城管理系统
 */
public interface ProductPropertyService {

    /**
     * 创建属性项
     * 注意，如果已经存在该属性项，直接返回它的编号即可
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProperty(@Valid ProductPropertyCreateReqVO createReqVO);

    /**
     * 更新属性项
     *
     * @param updateReqVO 更新信息
     */
    void updateProperty(@Valid ProductPropertyUpdateReqVO updateReqVO);

    /**
     * 删除属性项
     *
     * @param id 编号
     */
    void deleteProperty(Long id);

    /**
     * 获得属性项列表
     *
     * @param listReqVO 集合查询
     * @return 属性项集合
     */
    List<ProductPropertyDO> getPropertyList(ProductPropertyListReqVO listReqVO);

    /**
     * 获取属性名称分页
     *
     * @param pageReqVO 分页条件
     * @return 属性项分页
     */
    PageResult<ProductPropertyDO> getPropertyPage(ProductPropertyPageReqVO pageReqVO);

    /**
     * 获得指定编号的属性项
     *
     * @param id 编号
     * @return 属性项
     */
    ProductPropertyDO getProperty(Long id);

    /**
     * 根据属性项的编号的集合，获得对应的属性项数组
     *
     * @param ids 属性项的编号的集合
     * @return 属性项数组
     */
    List<ProductPropertyDO> getPropertyList(Collection<Long> ids);

}
