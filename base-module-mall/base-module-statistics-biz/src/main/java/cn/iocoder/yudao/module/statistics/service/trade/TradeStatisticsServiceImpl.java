package cn.iocoder.yudao.module.statistics.service.trade;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.DataComparisonRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.*;
import cn.iocoder.yudao.module.statistics.convert.trade.TradeStatisticsConvert;
import cn.iocoder.yudao.module.statistics.dal.dataobject.trade.TradeStatisticsDO;
import cn.iocoder.yudao.module.statistics.dal.mysql.trade.TradeStatisticsMapper;
import cn.iocoder.yudao.module.statistics.service.pay.PayWalletStatisticsService;
import cn.iocoder.yudao.module.statistics.service.trade.bo.AfterSaleSummaryRespBO;
import cn.iocoder.yudao.module.statistics.service.trade.bo.TradeOrderSummaryRespBO;
import cn.iocoder.yudao.module.statistics.service.trade.bo.TradeSummaryRespBO;
import cn.iocoder.yudao.module.statistics.service.trade.bo.WalletSummaryRespBO;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 交易统计 Service 实现类
 *
 * @author owen
 */
@Service
@Validated
public class TradeStatisticsServiceImpl implements TradeStatisticsService {

    @Resource
    private TradeStatisticsMapper tradeStatisticsMapper;

    @Resource
    private TradeOrderStatisticsService tradeOrderStatisticsService;
    @Resource
    private AfterSaleStatisticsService afterSaleStatisticsService;
    @Resource
    private BrokerageStatisticsService brokerageStatisticsService;
    @Resource
    private PayWalletStatisticsService payWalletStatisticsService;

    @Override
    public TradeSummaryRespBO getTradeSummaryByDays(int days) {
        LocalDateTime date = LocalDateTime.now().plusDays(days);
        return tradeStatisticsMapper.selectOrderCreateCountSumAndOrderPayPriceSumByTimeBetween(
                LocalDateTimeUtil.beginOfDay(date), LocalDateTimeUtil.endOfDay(date));
    }

    @Override
    public TradeSummaryRespBO getTradeSummaryByMonths(int months) {
        LocalDateTime monthDate = LocalDateTime.now().plusMonths(months);
        return tradeStatisticsMapper.selectOrderCreateCountSumAndOrderPayPriceSumByTimeBetween(
                LocalDateTimeUtils.beginOfMonth(monthDate), LocalDateTimeUtils.endOfMonth(monthDate));
    }

    @Override
    public PageResult<TradeDealReqVO> getDealTradePage(TradeDealPageReqVO pageReqVO) {
        PageResult<TradeDealReqVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }
        TradeSummaryRespBO deatData = tradeStatisticsMapper.getDealTradeDataByTimeBetween(startTime,endTime);
        TradeSummaryRespBO refundData = tradeStatisticsMapper.getRefundTradeDataByTimeBetween(startTime,endTime);
        TradeSummaryRespBO newData = tradeStatisticsMapper.getNewTradeDataByTimeBetween(startTime,endTime);
        TradeSummaryRespBO userCountData = tradeStatisticsMapper.getDealTradeUserCountByTimeBetween(startTime,endTime);
        TradeSummaryRespBO reBuyData = null;
        if (startTime!=null && endTime!=null){
            reBuyData = tradeStatisticsMapper.getReBuyDataByTimeBetween(startTime,endTime);
        }

        List<TradeDealReqVO> list = new ArrayList<>();
        TradeDealReqVO dealReqVO = new TradeDealReqVO();
        dealReqVO.setTotalUserCount(deatData.getCount());
        dealReqVO.setTotalDealAmount(deatData.getSummary());
        dealReqVO.setRefundUserCount(refundData.getCount());
        dealReqVO.setTotalRefundAmount(refundData.getSummary());
        dealReqVO.setNewUserCount(newData.getCount());

        //计算复购率
        String reBuyRate = "";
        Integer reBuyUserCount =0;
        if (reBuyData!=null){
            reBuyUserCount = reBuyData.getCount();
        }
        if ((dealReqVO.getTotalUserCount()!=null && dealReqVO.getTotalUserCount()>0) && (reBuyUserCount!=null)){
            BigDecimal tmp01 = new BigDecimal(reBuyUserCount);
            BigDecimal tmp02 = new BigDecimal(dealReqVO.getTotalUserCount());
            reBuyRate = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).toPlainString()+"%";
        }
        dealReqVO.setReBuyRate(reBuyRate);

        //计算客单价
        Integer avgAmount = null;
        Integer userCount = userCountData.getCount();
        if ((userCount!=null && userCount>0) && (dealReqVO.getTotalDealAmount()!=null)){
            BigDecimal tmp01 = new BigDecimal(dealReqVO.getTotalDealAmount());
            BigDecimal tmp02 = new BigDecimal(userCount);
            avgAmount = tmp01.divide(tmp02,2, RoundingMode.HALF_DOWN).intValue();
        }
        dealReqVO.setAvgAmount(avgAmount);
        list.add(dealReqVO);

        pageResult.setTotal(1L);
        pageResult.setList(list);
        return pageResult;
    }

    @Override
    public PageResult<TradeDetailReqVO> getDealDetailPage(TradeDealPageReqVO pageReqVO) {
        PageResult<TradeDetailReqVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }
        Long total = tradeStatisticsMapper.selectDealDetailCount(startTime, endTime);
        List<TradeDetailReqVO> list = new ArrayList<>();
        if (total!=null && total.compareTo(0L)>0){
            Integer startIndex = null;
            if (pageReqVO.getPageSize()>0){
                startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
            }
            list = tradeStatisticsMapper.selectDealDetailListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
            if (list==null){
                list = new ArrayList<>();
            }
        }

        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }

    @Override
    public PageResult<TradeTotalReqVO> getDealTotalPage(TradeDealPageReqVO pageReqVO) {
        PageResult<TradeTotalReqVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }
        Long total = tradeStatisticsMapper.selectDealTotalCount(startTime, endTime);
        List<TradeTotalReqVO> list = new ArrayList<>();
        if (total!=null && total.compareTo(0L)>0){
            Integer startIndex = null;
            if (pageReqVO.getPageSize()>0){
                startIndex = (pageReqVO.getPageNo()-1)*pageReqVO.getPageSize();
            }
            list = tradeStatisticsMapper.selectDealTotalListByCreateTimeBetween(startIndex,pageReqVO.getPageSize(),startTime, endTime);
            if (list==null){
                list = new ArrayList<>();
            }
        }

        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }

    @Override
    public DataComparisonRespVO<TradeTrendSummaryRespVO> getTradeTrendSummaryComparison(LocalDateTime beginTime,
                                                                                        LocalDateTime endTime) {
        // 统计数据
        TradeTrendSummaryRespVO value = tradeStatisticsMapper.selectVoByTimeBetween(beginTime, endTime);
        // 对照数据
        LocalDateTime referenceBeginTime = beginTime.minus(Duration.between(beginTime, endTime));
        TradeTrendSummaryRespVO reference = tradeStatisticsMapper.selectVoByTimeBetween(referenceBeginTime, beginTime);
        return TradeStatisticsConvert.INSTANCE.convert(value, reference);
    }

    @Override
    public Integer getExpensePrice(LocalDateTime beginTime, LocalDateTime endTime) {
        return tradeStatisticsMapper.selectExpensePriceByTimeBetween(beginTime, endTime);
    }

    @Override
    public List<TradeStatisticsDO> getTradeStatisticsList(LocalDateTime beginTime, LocalDateTime endTime) {
        return tradeStatisticsMapper.selectListByTimeBetween(beginTime, endTime);
    }

    @Override
    public String statisticsTrade(Integer days) {
        LocalDateTime today = LocalDateTime.now();
        return IntStream.rangeClosed(1, days)
                .mapToObj(day -> statisticsTrade(today.minusDays(day)))
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    /**
     * 统计交易数据
     *
     * @param date 需要统计的日期
     * @return 统计结果
     */
    private String statisticsTrade(LocalDateTime date) {
        // 1. 处理统计时间范围
        LocalDateTime beginTime = LocalDateTimeUtil.beginOfDay(date);
        LocalDateTime endTime = LocalDateTimeUtil.endOfDay(date);
        String dateStr = DatePattern.NORM_DATE_FORMAT.format(date);
        // 2. 检查该日是否已经统计过
        TradeStatisticsDO entity = tradeStatisticsMapper.selectByTimeBetween(beginTime, endTime);
        if (entity != null) {
            return dateStr + " 数据已存在，如果需要重新统计，请先删除对应的数据";
        }

        // 3. 从各个数据表，统计对应数据
        StopWatch stopWatch = new StopWatch(dateStr);
        // 3.1 统计订单
        stopWatch.start("统计订单");
        TradeOrderSummaryRespBO orderSummary = tradeOrderStatisticsService.getOrderSummary(beginTime, endTime);
        stopWatch.stop();
        // 3.2 统计售后
        stopWatch.start("统计售后");
        AfterSaleSummaryRespBO afterSaleSummary = afterSaleStatisticsService.getAfterSaleSummary(beginTime, endTime);
        stopWatch.stop();
        // 3.3 统计佣金
        stopWatch.start("统计佣金");
        Integer brokerageSettlementPrice = brokerageStatisticsService.getBrokerageSettlementPriceSummary(beginTime, endTime);
        stopWatch.stop();
        // 3.4 统计充值
        stopWatch.start("统计充值");
        WalletSummaryRespBO walletSummary = payWalletStatisticsService.getWalletSummary(beginTime, endTime);
        stopWatch.stop();

        // 4. 插入数据
        entity = TradeStatisticsConvert.INSTANCE.convert(date, orderSummary, afterSaleSummary, brokerageSettlementPrice,
                walletSummary);
        tradeStatisticsMapper.insert(entity);
        return stopWatch.prettyPrint();
    }

}
