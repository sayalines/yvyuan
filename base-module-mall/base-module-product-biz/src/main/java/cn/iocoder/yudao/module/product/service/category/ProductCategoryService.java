package cn.iocoder.yudao.module.product.service.category;

import cn.iocoder.yudao.module.product.controller.admin.category.vo.ProductCategoryCreateReqVO;
import cn.iocoder.yudao.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.yudao.module.product.controller.admin.category.vo.ProductCategoryUpdateReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.category.ProductCategoryDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 商品分类 Service 接口
 *
 * @author 商城管理系统
 */
public interface ProductCategoryService {

    /**
     * 创建商品分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCategory(@Valid ProductCategoryCreateReqVO createReqVO);

    /**
     * 更新商品分类
     *
     * @param updateReqVO 更新信息
     */
    void updateCategory(@Valid ProductCategoryUpdateReqVO updateReqVO);

    /**
     * 删除商品分类
     *
     * @param id 编号
     */
    void deleteCategory(Long id);

    /**
     * 获得商品分类
     *
     * @param id 编号
     * @return 商品分类
     */
    ProductCategoryDO getCategory(Long id);

    /**
     * 校验商品分类
     *
     * @param id 分类编号
     */
    void validateCategory(Long id);

    /**
     * 获得商品分类的层级
     *
     * @param id 编号
     * @return 商品分类的层级
     */
    Integer getCategoryLevel(Long id);

    /**
     * 获得商品分类列表
     *
     * @param listReqVO 查询条件
     * @return 商品分类列表
     */
    List<ProductCategoryDO> getEnableCategoryList(ProductCategoryListReqVO listReqVO);

    /**
     * 获得开启状态的商品分类列表
     *
     * @return 商品分类列表
     */
    List<ProductCategoryDO> getEnableCategoryList();

    /**
     * 校验商品分类是否有效。如下情况，视为无效：
     * 1. 商品分类编号不存在
     * 2. 商品分类被禁用
     *
     * @param ids 商品分类编号数组
     */
    void validateCategoryList(Collection<Long> ids);

    /**
     * 获得商品分类列表
     *
     * @param parentIds 查询条件
     * @return 商品分类列表
     */
    List<ProductCategoryDO> getEnableCategoryList(List<Long> parentIds);
}
