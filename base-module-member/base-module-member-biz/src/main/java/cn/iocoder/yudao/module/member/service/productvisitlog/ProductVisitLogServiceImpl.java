package cn.iocoder.yudao.module.member.service.productvisitlog;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.productvisitlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.productvisitlog.ProductVisitLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.member.dal.mysql.productvisitlog.ProductVisitLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 商品访问日志 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class ProductVisitLogServiceImpl implements ProductVisitLogService {

    @Resource
    private ProductVisitLogMapper productVisitLogMapper;

    @Override
    public Long createProductVisitLog(ProductVisitLogSaveReqVO createReqVO) {
        // 插入
        ProductVisitLogDO productVisitLog = BeanUtils.toBean(createReqVO, ProductVisitLogDO.class);
        productVisitLogMapper.insert(productVisitLog);
        // 返回
        return productVisitLog.getId();
    }

    @Override
    public void updateProductVisitLog(ProductVisitLogSaveReqVO updateReqVO) {
        // 校验存在
        validateProductVisitLogExists(updateReqVO.getId());
        // 更新
        ProductVisitLogDO updateObj = BeanUtils.toBean(updateReqVO, ProductVisitLogDO.class);
        productVisitLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductVisitLog(Long id) {
        // 校验存在
        validateProductVisitLogExists(id);
        // 删除
        productVisitLogMapper.deleteById(id);
    }

    private void validateProductVisitLogExists(Long id) {
        if (productVisitLogMapper.selectById(id) == null) {
            throw exception(PRODUCT_VISIT_LOG_NOT_EXISTS);
        }
    }

    @Override
    public ProductVisitLogDO getProductVisitLog(Long id) {
        return productVisitLogMapper.selectById(id);
    }

    @Override
    public PageResult<ProductVisitLogDO> getProductVisitLogPage(ProductVisitLogPageReqVO pageReqVO) {
        return productVisitLogMapper.selectPage(pageReqVO);
    }

}