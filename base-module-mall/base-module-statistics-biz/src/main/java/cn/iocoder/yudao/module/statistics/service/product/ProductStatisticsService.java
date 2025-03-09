package cn.iocoder.yudao.module.statistics.service.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductDetailRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductPageReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductRespVO;

/**
 * 商品统计
 */
public interface ProductStatisticsService {
    /**
     *获得商品汇总统计分页
     * @param pageReqVO
     * @return
     */
    PageResult<ProductRespVO> getProductPage(ProductPageReqVO pageReqVO);
    /**
     *获得商品明细统计分页
     * @param pageReqVO
     * @return
     */
    PageResult<ProductDetailRespVO> getProductDetailPage(ProductPageReqVO pageReqVO);
}
