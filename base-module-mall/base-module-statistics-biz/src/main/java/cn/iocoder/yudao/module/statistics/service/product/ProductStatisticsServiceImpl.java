package cn.iocoder.yudao.module.statistics.service.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.MemberActionRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductDetailRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductPageReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductRespVO;
import cn.iocoder.yudao.module.statistics.dal.mysql.product.ProductStatisticsMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品统计
 */
@Service
@Validated
public class ProductStatisticsServiceImpl implements ProductStatisticsService {

    @Resource
    private ProductStatisticsMapper productStatisticsMapper;

    @Override
    public PageResult<ProductRespVO> getProductPage(ProductPageReqVO pageReqVO) {
        PageResult<ProductRespVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }
        Long total = 1L;
        List<ProductRespVO> list = new ArrayList<>();
        ProductRespVO respVO = new ProductRespVO();
        //查询实时数据
        if (pageReqVO.getIsRealTime()==null || pageReqVO.getIsRealTime()){
            //获取商品统计
            respVO = productStatisticsMapper.selectProductListByCreateTimeBetween(startTime, endTime);
            if (respVO==null){
                respVO = new ProductRespVO();
            }

            if (respVO.getVisitCount()==null){
                respVO.setVisitCount(0);
            }

            if (respVO.getHitCount()==null){
                respVO.setHitCount(0);
            }

            if (respVO.getCartUserCount()==null){
                respVO.setCartUserCount(0);
            }

            if (respVO.getCartCount()==null){
                respVO.setCartCount(0);
            }

            if (respVO.getOrderUserCount()==null){
                respVO.setOrderUserCount(0);
            }

            if (respVO.getPayUserCount()==null){
                respVO.setPayUserCount(0);
            }

            if (respVO.getPayAmount()==null){
                respVO.setPayAmount(BigDecimal.ZERO);
            }

            if (respVO.getPayOrderCount()==null){
                respVO.setPayOrderCount(0);
            }


            //获取添加购物车人数
            ProductRespVO cartData = productStatisticsMapper.selectAddCartListByCreateTimeBetween(startTime, endTime);
            if (cartData!=null){
                if (cartData.getCartUserCount()!=null){
                    respVO.setCartUserCount(cartData.getCartUserCount());
                }
                if (cartData.getCartCount()!=null){
                    respVO.setCartCount(cartData.getCartCount());
                }
            }
        }else{
            //查询汇总数据
            //获取商品统计
            respVO = productStatisticsMapper.selectProductStatListByCreateTimeBetween(startTime, endTime);
            if (respVO==null){
                respVO = new ProductRespVO();
            }

            if (respVO.getVisitCount()==null){
                respVO.setVisitCount(0);
            }

            if (respVO.getHitCount()==null){
                respVO.setHitCount(0);
            }

            if (respVO.getCartUserCount()==null){
                respVO.setCartUserCount(0);
            }

            if (respVO.getCartCount()==null){
                respVO.setCartCount(0);
            }

            if (respVO.getOrderUserCount()==null){
                respVO.setOrderUserCount(0);
            }

            if (respVO.getPayUserCount()==null){
                respVO.setPayUserCount(0);
            }

            if (respVO.getPayAmount()==null){
                respVO.setPayAmount(BigDecimal.ZERO);
            }

            if (respVO.getPayOrderCount()==null){
                respVO.setPayOrderCount(0);
            }


            //获取添加购物车人数
            ProductRespVO cartData = productStatisticsMapper.selectAddCartStatListByCreateTimeBetween(startTime, endTime);
            if (cartData!=null){
                if (cartData.getCartUserCount()!=null){
                    respVO.setCartUserCount(cartData.getCartUserCount());
                }
                if (cartData.getCartCount()!=null){
                    respVO.setCartCount(cartData.getCartCount());
                }
            }
        }

        //获取订单数据
        ProductRespVO orderData = productStatisticsMapper.selectOrderListByCreateTimeBetween(startTime, endTime);
        if (orderData!=null){
            if (orderData.getOrderUserCount()!=null){
                respVO.setOrderUserCount(orderData.getOrderUserCount());
            }
        }
        //获取支付订单数据
        ProductRespVO orderPayData = productStatisticsMapper.selectPayOrderListByCreateTimeBetween(startTime, endTime);
        if (orderPayData!=null){
            if (orderPayData.getPayOrderCount()!=null){
                respVO.setPayOrderCount(orderPayData.getPayOrderCount());
            }
            if (orderPayData.getPayUserCount()!=null){
                respVO.setPayUserCount(orderPayData.getPayUserCount());
            }
            if (orderPayData.getPayAmount()!=null){
                respVO.setPayAmount(orderPayData.getPayAmount());
            }
            if (orderPayData.getDeliveryTime()!=null){
                respVO.setDeliveryTime(orderPayData.getDeliveryTime());
            }
        }
        //计算加购率
        String cartRate = "";
        if ((respVO.getVisitCount()!=null && respVO.getVisitCount()>0) && (respVO.getCartUserCount()!=null)){
            BigDecimal tmp01 = new BigDecimal(respVO.getCartUserCount());
            BigDecimal tmp02 = new BigDecimal(respVO.getVisitCount());
            cartRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).toPlainString()+"%";
        }
        respVO.setCartRate(cartRate);
        //计算商品点击率
        String hitRate = "";
        if ((respVO.getVisitCount()!=null && respVO.getVisitCount()>0) && (respVO.getHitCount()!=null)){
            BigDecimal tmp01 = new BigDecimal(respVO.getHitCount());
            BigDecimal tmp02 = new BigDecimal(respVO.getVisitCount());
            hitRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).toPlainString();
        }
        respVO.setHitRate(hitRate);
        list.add(respVO);

        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }

    @Override
    public PageResult<ProductDetailRespVO> getProductDetailPage(ProductPageReqVO pageReqVO) {
        PageResult<ProductDetailRespVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }
        Long total = 0L;
        List<ProductDetailRespVO> list = new ArrayList<>();
        //查询实时数据
        if (pageReqVO.getIsRealTime()==null || pageReqVO.getIsRealTime()){
            total = productStatisticsMapper.selectProductDetailCount(startTime, endTime);
            if (total!=null && total.compareTo(0L)>0){
                Integer startIndex = null;
                if (pageReqVO.getPageSize()>0){
                    startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
                }
                List<Long> spuIds = new ArrayList<>();
                list = productStatisticsMapper.selectProductDetailListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
                if (list==null){
                    list=new ArrayList<>();
                }

                if (list.size()>0){
                    for(ProductDetailRespVO dd:list){
                        if (dd.getVisitCount()==null){
                            dd.setVisitCount(0);
                        }
                        if (dd.getHitCount()==null){
                            dd.setHitCount(0);
                        }
                        if (dd.getCartUserCount()==null){
                            dd.setCartUserCount(0);
                        }
                        if (dd.getCartCount()==null){
                            dd.setCartCount(0);
                        }
                        if (dd.getOrderUserCount()==null){
                            dd.setOrderUserCount(0);
                        }
                        if (dd.getPayUserCount()==null){
                            dd.setPayUserCount(0);
                        }
                        if (dd.getPayAmount()==null){
                            dd.setPayAmount(BigDecimal.ZERO);
                        }
                        if (dd.getPayOrderCount()==null){
                            dd.setPayOrderCount(0);
                        }

                        spuIds.add(Long.valueOf(dd.getSpuId()));
                    }

                    //获取添加购物车人数
                    List<ProductDetailRespVO> cartDataList = productStatisticsMapper.selectAddCartDetailListByCreateTimeBetween(spuIds,startTime, endTime);

                    //获取订单数据
                    List<ProductDetailRespVO>  orderDataList = productStatisticsMapper.selectOrderDetailListByCreateTimeBetween(spuIds,startTime, endTime);

                    //获取支付订单数据
                    List<ProductDetailRespVO> orderPayDataList = productStatisticsMapper.selectPayOrderDetailListByCreateTimeBetween(spuIds,startTime, endTime);

                    //拼装数据
                    for(ProductDetailRespVO dd:list){
                        String spuId = dd.getSpuId();
                        if (cartDataList!=null && cartDataList.size()>0){
                            for(ProductDetailRespVO cartData:cartDataList){
                                if (spuId.equalsIgnoreCase(cartData.getSpuId())){
                                    if (cartData.getCartUserCount()!=null){
                                        dd.setCartUserCount(cartData.getCartUserCount());
                                    }
                                    if (cartData.getCartCount()!=null){
                                        dd.setCartCount(cartData.getCartCount());
                                    }
                                    break;
                                }
                            }
                        }

                        if (orderDataList!=null && orderDataList.size()>0){
                            for(ProductDetailRespVO orderData:orderDataList){
                                if (spuId.equalsIgnoreCase(orderData.getSpuId())){
                                    if (orderData.getOrderUserCount()!=null){
                                        dd.setOrderUserCount(orderData.getOrderUserCount());
                                    }
                                    break;
                                }
                            }
                        }

                        if (orderPayDataList!=null && orderPayDataList.size()>0){
                            for(ProductDetailRespVO orderPayData:orderPayDataList) {
                                if (spuId.equalsIgnoreCase(orderPayData.getSpuId())) {
                                    if (orderPayData.getPayOrderCount()!=null){
                                        dd.setPayOrderCount(orderPayData.getPayOrderCount());
                                    }
                                    if (orderPayData.getPayUserCount()!=null){
                                        dd.setPayUserCount(orderPayData.getPayUserCount());
                                    }
                                    if (orderPayData.getPayAmount()!=null){
                                        dd.setPayAmount(orderPayData.getPayAmount());
                                    }
                                    if (orderPayData.getDeliveryTime()!=null){
                                        dd.setDeliveryTime(orderPayData.getDeliveryTime());
                                    }
                                }
                            }
                        }

                        //计算加购率
                        String cartRate = "";
                        if ((dd.getVisitCount()!=null && dd.getVisitCount()>0) && (dd.getCartUserCount()!=null)){
                            BigDecimal tmp01 = new BigDecimal(dd.getCartUserCount());
                            BigDecimal tmp02 = new BigDecimal(dd.getVisitCount());
                            cartRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).toPlainString()+"%";
                        }
                        dd.setCartRate(cartRate);

                        //计算商品点击率
                        String hitRate = "";
                        if ((dd.getVisitCount()!=null && dd.getVisitCount()>0) && (dd.getHitCount()!=null)){
                            BigDecimal tmp01 = new BigDecimal(dd.getHitCount());
                            BigDecimal tmp02 = new BigDecimal(dd.getVisitCount());
                            hitRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).toPlainString();
                        }
                        dd.setHitRate(hitRate);
                    }
                }
            }
        }else{
            //查询汇总数据
            total = productStatisticsMapper.selectProductDetailStatCount(startTime, endTime);
            if (total!=null && total.compareTo(0L)>0){
                Integer startIndex = null;
                if (pageReqVO.getPageSize()>0){
                    startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
                }
                List<Long> spuIds = new ArrayList<>();
                list = productStatisticsMapper.selectProductDetailStatListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
                if (list==null){
                    list=new ArrayList<>();
                }

                if (list.size()>0){
                    for(ProductDetailRespVO dd:list){
                        if (dd.getVisitCount()==null){
                            dd.setVisitCount(0);
                        }
                        if (dd.getHitCount()==null){
                            dd.setHitCount(0);
                        }
                        if (dd.getCartUserCount()==null){
                            dd.setCartUserCount(0);
                        }
                        if (dd.getCartCount()==null){
                            dd.setCartCount(0);
                        }
                        if (dd.getOrderUserCount()==null){
                            dd.setOrderUserCount(0);
                        }
                        if (dd.getPayUserCount()==null){
                            dd.setPayUserCount(0);
                        }
                        if (dd.getPayAmount()==null){
                            dd.setPayAmount(BigDecimal.ZERO);
                        }
                        if (dd.getPayOrderCount()==null){
                            dd.setPayOrderCount(0);
                        }

                        spuIds.add(Long.valueOf(dd.getSpuId()));
                    }

                    //获取添加购物车人数
                    List<ProductDetailRespVO> cartDataList = productStatisticsMapper.selectAddCartDetailStatListByCreateTimeBetween(spuIds,startTime, endTime);

                    //获取订单数据
                    List<ProductDetailRespVO>  orderDataList = productStatisticsMapper.selectOrderDetailListByCreateTimeBetween(spuIds,startTime, endTime);

                    //获取支付订单数据
                    List<ProductDetailRespVO> orderPayDataList = productStatisticsMapper.selectPayOrderDetailListByCreateTimeBetween(spuIds,startTime, endTime);

                    //拼装数据
                    for(ProductDetailRespVO dd:list){
                        String spuId = dd.getSpuId();
                        if (cartDataList!=null && cartDataList.size()>0){
                            for(ProductDetailRespVO cartData:cartDataList){
                                if (spuId.equalsIgnoreCase(cartData.getSpuId())){
                                    if (cartData.getCartUserCount()!=null){
                                        dd.setCartUserCount(cartData.getCartUserCount());
                                    }
                                    if (cartData.getCartCount()!=null){
                                        dd.setCartCount(cartData.getCartCount());
                                    }
                                    break;
                                }
                            }
                        }

                        if (orderDataList!=null && orderDataList.size()>0){
                            for(ProductDetailRespVO orderData:orderDataList){
                                if (spuId.equalsIgnoreCase(orderData.getSpuId())){
                                    if (orderData.getOrderUserCount()!=null){
                                        dd.setOrderUserCount(orderData.getOrderUserCount());
                                    }
                                    break;
                                }
                            }
                        }

                        if (orderPayDataList!=null && orderPayDataList.size()>0){
                            for(ProductDetailRespVO orderPayData:orderPayDataList) {
                                if (spuId.equalsIgnoreCase(orderPayData.getSpuId())) {
                                    if (orderPayData.getPayOrderCount()!=null){
                                        dd.setPayOrderCount(orderPayData.getPayOrderCount());
                                    }
                                    if (orderPayData.getPayUserCount()!=null){
                                        dd.setPayUserCount(orderPayData.getPayUserCount());
                                    }
                                    if (orderPayData.getPayAmount()!=null){
                                        dd.setPayAmount(orderPayData.getPayAmount());
                                    }
                                    if (orderPayData.getDeliveryTime()!=null){
                                        dd.setDeliveryTime(orderPayData.getDeliveryTime());
                                    }
                                }
                            }
                        }

                        //计算加购率
                        String cartRate = "";
                        if ((dd.getVisitCount()!=null && dd.getVisitCount()>0) && (dd.getCartUserCount()!=null)){
                            BigDecimal tmp01 = new BigDecimal(dd.getCartUserCount());
                            BigDecimal tmp02 = new BigDecimal(dd.getVisitCount());
                            cartRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).toPlainString()+"%";
                        }
                        dd.setCartRate(cartRate);

                        //计算商品点击率
                        String hitRate = "";
                        if ((dd.getVisitCount()!=null && dd.getVisitCount()>0) && (dd.getHitCount()!=null)){
                            BigDecimal tmp01 = new BigDecimal(dd.getHitCount());
                            BigDecimal tmp02 = new BigDecimal(dd.getVisitCount());
                            hitRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).toPlainString();
                        }
                        dd.setHitRate(hitRate);
                    }
                }
            }
        }


        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }
}
