package cn.iocoder.yudao.module.product.service.property;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.controller.admin.property.vo.property.ProductPropertyCreateReqVO;
import cn.iocoder.yudao.module.product.controller.admin.property.vo.property.ProductPropertyListReqVO;
import cn.iocoder.yudao.module.product.controller.admin.property.vo.property.ProductPropertyPageReqVO;
import cn.iocoder.yudao.module.product.controller.admin.property.vo.property.ProductPropertyUpdateReqVO;
import cn.iocoder.yudao.module.product.convert.property.ProductPropertyConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.property.ProductPropertyDO;
import cn.iocoder.yudao.module.product.dal.mysql.property.ProductPropertyMapper;
import cn.iocoder.yudao.module.product.service.sku.ProductSkuService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.*;

/**
 * 商品属性项 Service 实现类
 *
 * @author 商城管理系统
 */
@Service
@Validated
public class ProductPropertyServiceImpl implements ProductPropertyService {

    @Resource
    private ProductPropertyMapper productPropertyMapper;

    @Resource
    @Lazy // 延迟加载，解决循环依赖问题
    private ProductPropertyValueService productPropertyValueService;

    @Resource
    private ProductSkuService productSkuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProperty(ProductPropertyCreateReqVO createReqVO) {
        // 如果已经添加过该属性项，直接返回
        ProductPropertyDO dbProperty = productPropertyMapper.selectByName(createReqVO.getName());
        if (dbProperty != null) {
            return dbProperty.getId();
        }

        // 插入
        ProductPropertyDO property = ProductPropertyConvert.INSTANCE.convert(createReqVO);
        productPropertyMapper.insert(property);
        // 返回
        return property.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProperty(ProductPropertyUpdateReqVO updateReqVO) {
        validatePropertyExists(updateReqVO.getId());
        // 校验名字重复
        ProductPropertyDO productPropertyDO = productPropertyMapper.selectByName(updateReqVO.getName());
        if (productPropertyDO != null &&
                ObjUtil.notEqual(productPropertyDO.getId(), updateReqVO.getId())) {
            throw exception(PROPERTY_EXISTS);
        }

        // 更新
        ProductPropertyDO updateObj = ProductPropertyConvert.INSTANCE.convert(updateReqVO);
        productPropertyMapper.updateById(updateObj);
        // 更新 sku 相关属性
        productSkuService.updateSkuProperty(updateObj.getId(), updateObj.getName());
    }

    @Override
    public void deleteProperty(Long id) {
        // 校验存在
        validatePropertyExists(id);
        // 校验其下是否有规格值
        if (productPropertyValueService.getPropertyValueCountByPropertyId(id) > 0) {
            throw exception(PROPERTY_DELETE_FAIL_VALUE_EXISTS);
        }

        // 删除
        productPropertyMapper.deleteById(id);
        // 同步删除属性值
        productPropertyValueService.deletePropertyValueByPropertyId(id);
    }

    private void validatePropertyExists(Long id) {
        if (productPropertyMapper.selectById(id) == null) {
            throw exception(PROPERTY_NOT_EXISTS);
        }
    }

    @Override
    public List<ProductPropertyDO> getPropertyList(ProductPropertyListReqVO listReqVO) {
        return productPropertyMapper.selectList(listReqVO);
    }

    @Override
    public PageResult<ProductPropertyDO> getPropertyPage(ProductPropertyPageReqVO pageReqVO) {
        return productPropertyMapper.selectPage(pageReqVO);
    }

    @Override
    public ProductPropertyDO getProperty(Long id) {
        return productPropertyMapper.selectById(id);
    }

    @Override
    public List<ProductPropertyDO> getPropertyList(Collection<Long> ids) {
        return productPropertyMapper.selectBatchIds(ids);
    }

}
