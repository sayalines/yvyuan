package cn.iocoder.yudao.module.statistics.dal.mysql.trade;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeDetailReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeTotalReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeTrendSummaryRespVO;
import cn.iocoder.yudao.module.statistics.dal.dataobject.trade.TradeStatisticsDO;
import cn.iocoder.yudao.module.statistics.service.trade.bo.TradeSummaryRespBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易统计 Mapper
 *
 * @author owen
 */
@Mapper
public interface TradeStatisticsMapper extends BaseMapperX<TradeStatisticsDO> {

    TradeSummaryRespBO selectOrderCreateCountSumAndOrderPayPriceSumByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                                                 @Param("endTime") LocalDateTime endTime);

    TradeTrendSummaryRespVO selectVoByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                  @Param("endTime") LocalDateTime endTime);

    // TODO @芋艿：已经 review
    default List<TradeStatisticsDO> selectListByTimeBetween(LocalDateTime beginTime, LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapperX<TradeStatisticsDO>()
                .between(TradeStatisticsDO::getTime, beginTime, endTime));
    }

    // TODO @芋艿：已经 review
    Integer selectExpensePriceByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                            @Param("endTime") LocalDateTime endTime);

    // TODO @芋艿：已经 review
    default TradeStatisticsDO selectByTimeBetween(LocalDateTime beginTime, LocalDateTime endTime) {
        return selectOne(new LambdaQueryWrapperX<TradeStatisticsDO>()
                .between(TradeStatisticsDO::getTime, beginTime, endTime));
    }

    //总成交人数、总成交金额
    TradeSummaryRespBO getDealTradeDataByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime);

    //退款人数、总退款金额
    TradeSummaryRespBO getRefundTradeDataByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime);

    //新买家人数、新买家金额
    TradeSummaryRespBO getNewTradeDataByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime);

    //复购人数、复购金额
    TradeSummaryRespBO getReBuyDataByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime);

    //总成交人数(不含发货单)
    TradeSummaryRespBO getDealTradeUserCountByTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                     @Param("endTime") LocalDateTime endTime);


    /**
     * 获取交易明细数
     * @param beginTime
     * @param endTime
     * @return
     */
    Long selectDealDetailCount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
    /**
     * 获取交易明细统计
     * @return
     */
    List<TradeDetailReqVO> selectDealDetailListByCreateTimeBetween(@Param("startIndex") Integer startIndex,
                                                                  @Param("pageSize") Integer pageSize,
                                                                  @Param("beginTime") LocalDateTime beginTime,
                                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 获取交易汇总数
     * @param beginTime
     * @param endTime
     * @return
     */
    Long selectDealTotalCount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
    /**
     * 获取交易汇总统计
     * @return
     */
    List<TradeTotalReqVO> selectDealTotalListByCreateTimeBetween(@Param("startIndex") Integer startIndex,
                                                                 @Param("pageSize") Integer pageSize,
                                                                 @Param("beginTime") LocalDateTime beginTime,
                                                                 @Param("endTime") LocalDateTime endTime);


}
