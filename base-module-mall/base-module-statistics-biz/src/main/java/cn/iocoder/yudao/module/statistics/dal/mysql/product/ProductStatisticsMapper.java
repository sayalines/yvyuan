package cn.iocoder.yudao.module.statistics.dal.mysql.product;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductDetailRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品统计 Mapper
 *
 * @author owen
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface ProductStatisticsMapper extends BaseMapperX {


    /**
     * 获取商品统计
     * @return
     */
    ProductRespVO selectProductListByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,@Param("endTime") LocalDateTime endTime);

    /**
     * 获取商品统计(汇总)
     * @return
     */
    ProductRespVO selectProductStatListByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,@Param("endTime") LocalDateTime endTime);

    /**
     * 获取添加购物车人数
     * @param beginTime
     * @param endTime
     * @return
     */
    ProductRespVO selectAddCartListByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取添加购物车人数(汇总)
     * @param beginTime
     * @param endTime
     * @return
     */
    ProductRespVO selectAddCartStatListByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 获取订单数据
     * @param beginTime
     * @param endTime
     * @return
     */
    ProductRespVO selectOrderListByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 获取支付订单数据
     * @param beginTime
     * @param endTime
     * @return
     */
    ProductRespVO selectPayOrderListByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取商品明细数
     * @param beginTime
     * @param endTime
     * @return
     */
    Long selectProductDetailCount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取商品明细统计
     * @return
     */
    List<ProductDetailRespVO> selectProductDetailListByCreateTimeBetween(@Param("startIndex") Integer startIndex,
                                                                   @Param("pageSize") Integer pageSize,
                                                                   @Param("beginTime") LocalDateTime beginTime,
                                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 获取明细添加购物车人数
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductDetailRespVO>  selectAddCartDetailListByCreateTimeBetween(
            @Param("spuIds") List<Long> spuIds,
            @Param("beginTime") LocalDateTime beginTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 获取商品明细数(汇总)
     * @param beginTime
     * @param endTime
     * @return
     */
    Long selectProductDetailStatCount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取商品明细统计(汇总)
     * @return
     */
    List<ProductDetailRespVO> selectProductDetailStatListByCreateTimeBetween(@Param("startIndex") Integer startIndex,
                                                                         @Param("pageSize") Integer pageSize,
                                                                         @Param("beginTime") LocalDateTime beginTime,
                                                                         @Param("endTime") LocalDateTime endTime);
    /**
     * 获取明细添加购物车人数(汇总)
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductDetailRespVO>  selectAddCartDetailStatListByCreateTimeBetween(
                                                        @Param("spuIds") List<Long> spuIds,
                                                        @Param("beginTime") LocalDateTime beginTime,
                                                       @Param("endTime") LocalDateTime endTime);
    /**
     * 获取订单明细数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductDetailRespVO>  selectOrderDetailListByCreateTimeBetween(
                @Param("spuIds") List<Long> spuIds,
                @Param("beginTime") LocalDateTime beginTime,
                @Param("endTime") LocalDateTime endTime);

    /**
     * 获取支付订单明细数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductDetailRespVO> selectPayOrderDetailListByCreateTimeBetween(
            @Param("spuIds") List<Long> spuIds,
            @Param("beginTime") LocalDateTime beginTime,
            @Param("endTime") LocalDateTime endTime);
}
