package cn.iocoder.yudao.module.statistics.controller.admin.trade;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.DataComparisonRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.*;
import cn.iocoder.yudao.module.statistics.convert.trade.TradeStatisticsConvert;
import cn.iocoder.yudao.module.statistics.dal.dataobject.trade.TradeStatisticsDO;
import cn.iocoder.yudao.module.statistics.service.trade.AfterSaleStatisticsService;
import cn.iocoder.yudao.module.statistics.service.trade.BrokerageStatisticsService;
import cn.iocoder.yudao.module.statistics.service.trade.TradeOrderStatisticsService;
import cn.iocoder.yudao.module.statistics.service.trade.TradeStatisticsService;
import cn.iocoder.yudao.module.statistics.service.trade.bo.TradeSummaryRespBO;
import cn.iocoder.yudao.module.trade.enums.aftersale.AfterSaleStatusEnum;
import cn.iocoder.yudao.module.trade.enums.delivery.DeliveryTypeEnum;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 交易统计")
@RestController
@RequestMapping("/statistics/trade")
@Validated
@Slf4j
public class TradeStatisticsController {

    @Resource
    private TradeStatisticsService tradeStatisticsService;
    @Resource
    private TradeOrderStatisticsService tradeOrderStatisticsService;
    @Resource
    private AfterSaleStatisticsService afterSaleStatisticsService;
    @Resource
    private BrokerageStatisticsService brokerageStatisticsService;

    // TODO 芋艿：已经 review
    @GetMapping("/summary")
    @Operation(summary = "获得交易统计")
    @PreAuthorize("@ss.hasPermission('statistics:trade:query')")
    public CommonResult<DataComparisonRespVO<TradeSummaryRespVO>> getTradeSummaryComparison() {
        // 1.1 昨天的数据
        TradeSummaryRespBO yesterdayData = tradeStatisticsService.getTradeSummaryByDays(-1);
        // 1.2 前天的数据（用于对照昨天的数据）
        TradeSummaryRespBO beforeYesterdayData = tradeStatisticsService.getTradeSummaryByDays(-2);

        // 2.1 本月数据
        TradeSummaryRespBO monthData = tradeStatisticsService.getTradeSummaryByMonths(0);
        // 2.2 上月数据（用于对照本月的数据）
        TradeSummaryRespBO lastMonthData = tradeStatisticsService.getTradeSummaryByMonths(-1);
        // 拼接数据
        return success(TradeStatisticsConvert.INSTANCE.convert(yesterdayData, beforeYesterdayData, monthData, lastMonthData));
    }

    // TODO @疯狂：【晚点再改和讨论；等首页的接口出来】这个要不还是叫 analyse，对比选中的时间段，和上一个时间段；类似 MemberStatisticsController 的 getMemberAnalyse
    @GetMapping("/trend/summary")
    @Operation(summary = "获得交易状况统计")
    @PreAuthorize("@ss.hasPermission('statistics:trade:query')")
    public CommonResult<DataComparisonRespVO<TradeTrendSummaryRespVO>> getTradeTrendSummaryComparison(
            TradeTrendReqVO reqVO) {
        return success(tradeStatisticsService.getTradeTrendSummaryComparison(ArrayUtil.get(reqVO.getTimes(), 0),
                ArrayUtil.get(reqVO.getTimes(), 1)));
    }

    // TODO 芋艿：已经 review
    @GetMapping("/list")
    @Operation(summary = "获得交易状况明细")
    @PreAuthorize("@ss.hasPermission('statistics:trade:query')")
    public CommonResult<List<TradeTrendSummaryRespVO>> getTradeStatisticsList(TradeTrendReqVO reqVO) {
        List<TradeStatisticsDO> list = tradeStatisticsService.getTradeStatisticsList(ArrayUtil.get(reqVO.getTimes(), 0),
                ArrayUtil.get(reqVO.getTimes(), 1));
        return success(TradeStatisticsConvert.INSTANCE.convertList(list));
    }

    // TODO 芋艿：已经 review
    @GetMapping("/export-excel")
    @Operation(summary = "导出获得交易状况明细 Excel")
    @PreAuthorize("@ss.hasPermission('statistics:trade:export')")
    public void exportTradeStatisticsExcel(TradeTrendReqVO reqVO, HttpServletResponse response) throws IOException {
        List<TradeStatisticsDO> list = tradeStatisticsService.getTradeStatisticsList(ArrayUtil.get(reqVO.getTimes(), 0),
                ArrayUtil.get(reqVO.getTimes(), 1));
        // 导出 Excel
        List<TradeTrendSummaryRespVO> voList = TradeStatisticsConvert.INSTANCE.convertList(list);
        List<TradeTrendSummaryExcelVO> data = TradeStatisticsConvert.INSTANCE.convertList02(voList);
        ExcelUtils.write(response, "交易状况.xls", "数据", TradeTrendSummaryExcelVO.class, data);
    }

    // TODO 芋艿：已经 review
    @GetMapping("/order-count")
    @Operation(summary = "获得交易订单数量")
    @PreAuthorize("@ss.hasPermission('statistics:trade:query')")
    public CommonResult<TradeOrderCountRespVO> getOrderCount() {
        // 订单统计
        Long undeliveredCount = tradeOrderStatisticsService.selectCountByStatusAndDeliveryTypeByCommon(
                TradeOrderStatusEnum.UNDELIVERED.getStatus(), DeliveryTypeEnum.EXPRESS.getType());
        // TODO @疯狂：订单支付后，如果是门店自提的，需要 update 成 DELIVERED；；目前还没搞~~突然反应过来
//        Long pickUpCount = tradeOrderStatisticsService.getCountByStatusAndDeliveryType(
//                TradeOrderStatusEnum.DELIVERED.getStatus(), DeliveryTypeEnum.PICK_UP.getType());
        Long pickUpCount =0L;
        // 售后统计
        Long afterSaleApplyCount = afterSaleStatisticsService.getCountByStatus(AfterSaleStatusEnum.APPLY);
//        Long auditingWithdrawCount = brokerageStatisticsService.getWithdrawCountByStatus(BrokerageWithdrawStatusEnum.AUDITING);
        Long auditingWithdrawCount =0L;
        // 拼接返回
        TradeOrderCountRespVO respVO = TradeStatisticsConvert.INSTANCE.convert(undeliveredCount, pickUpCount, afterSaleApplyCount, auditingWithdrawCount);
        return success(respVO);
    }

    // TODO 芋艿：已经 review
    @GetMapping("/order-comparison")
    @Operation(summary = "获得交易订单数量")
    @PreAuthorize("@ss.hasPermission('statistics:trade:query')")
    public CommonResult<DataComparisonRespVO<TradeOrderSummaryRespVO>> getOrderComparison() {
        return success(tradeOrderStatisticsService.getOrderComparison());
    }

    // TODO 芋艿：已经 review
    @GetMapping("/order-count-trend")
    @Operation(summary = "获得订单量趋势统计")
    @PreAuthorize("@ss.hasPermission('statistics:trade:query')")
    public CommonResult<List<DataComparisonRespVO<TradeOrderTrendRespVO>>> getOrderCountTrendComparison(@Valid TradeOrderTrendReqVO reqVO) {
        // TODO @疯狂：要注意 date 的排序；
        return success(tradeOrderStatisticsService.getOrderCountTrendComparison(reqVO));
    }

    @GetMapping("/deal/page")
    @Operation(summary = "获得成交数据分页")
    @PreAuthorize("@ss.hasPermission('statistics:trade:query')")
    public CommonResult<PageResult<TradeDealReqVO>> getDealTradePage(@Valid TradeDealPageReqVO pageReqVO) {
        return success(tradeStatisticsService.getDealTradePage(pageReqVO));
    }

    @GetMapping("/deal/export-excel")
    @Operation(summary = "导出成交数据 Excel")
    @PreAuthorize("@ss.hasPermission('statistics:trade:export')")
    public void exportDealTradeExcel(TradeDealPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        List<TradeDealReqVO> list = tradeStatisticsService.getDealTradePage(pageReqVO).getList();
        List<TradeDealExcelReqVO> resList = new ArrayList<>();
        if (list!=null && list.size()>0){
            for(TradeDealReqVO dd:list){
                TradeDealExcelReqVO dto = new TradeDealExcelReqVO();
                dto.setTotalUserCount(dd.getTotalUserCount());
                dto.setNewUserCount(dd.getNewUserCount());
                dto.setRefundUserCount(dd.getRefundUserCount());
                dto.setTotalDealAmount(formatAmount(dd.getTotalDealAmount()));
                dto.setTotalRefundAmount(formatAmount(dd.getTotalRefundAmount()));
                dto.setAvgAmount(formatAmount(dd.getAvgAmount()));
                dto.setReBuyRate(dd.getReBuyRate());
                resList.add(dto);
            }
        }
        ExcelUtils.write(response, "成交数据.xls", "统计", TradeDealExcelReqVO.class, resList);
    }

    @GetMapping("/deal/detail/page")
    @Operation(summary = "获得交易明细分页")
    @PreAuthorize("@ss.hasPermission('statistics:tradeDetail:query')")
    public CommonResult<PageResult<TradeDetailReqVO>> getDealDetailPage(@Valid TradeDealPageReqVO pageReqVO) {
        return success(tradeStatisticsService.getDealDetailPage(pageReqVO));
    }

    @GetMapping("/deal/detail/export-excel")
    @Operation(summary = "导出交易明细Excel")
    @PreAuthorize("@ss.hasPermission('statistics:tradeDetail:export')")
    public void exportDealDetailExcel(TradeDealPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TradeDetailReqVO> list = tradeStatisticsService.getDealDetailPage(pageReqVO).getList();
        ExcelUtils.write(response, "交易明细.xls", "统计", TradeDetailReqVO.class, list);
    }

    @GetMapping("/deal/total/page")
    @Operation(summary = "获得交易汇总分页")
    @PreAuthorize("@ss.hasPermission('statistics:tradeTotal:query')")
    public CommonResult<PageResult<TradeTotalReqVO>> getDealTotalPage(@Valid TradeDealPageReqVO pageReqVO) {
        return success(tradeStatisticsService.getDealTotalPage(pageReqVO));
    }

    @GetMapping("/deal/total/export-excel")
    @Operation(summary = "导出交易汇总Excel")
    @PreAuthorize("@ss.hasPermission('statistics:tradeTotal:export')")
    public void exportDealTotalExcel(TradeDealPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TradeTotalReqVO> list = tradeStatisticsService.getDealTotalPage(pageReqVO).getList();
        ExcelUtils.write(response, "交易汇总.xls", "统计", TradeTotalReqVO.class, list);
    }

    public BigDecimal formatAmount(Integer data){
        BigDecimal result = BigDecimal.ZERO;
        if (data!=null){
            result = new BigDecimal(data);
            result = result.divide(new BigDecimal(100),2, RoundingMode.HALF_DOWN);
        }
        return result;
    };

}
