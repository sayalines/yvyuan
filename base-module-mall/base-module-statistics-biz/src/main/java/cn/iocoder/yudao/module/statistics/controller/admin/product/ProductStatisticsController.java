package cn.iocoder.yudao.module.statistics.controller.admin.product;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.MemberActionExcelVO;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.MemberActionPageReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.MemberActionRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.*;
import cn.iocoder.yudao.module.statistics.dal.mysql.product.ProductSpuStatisticsDO;
import cn.iocoder.yudao.module.statistics.dal.mysql.product.ProductStatisticsDO;
import cn.iocoder.yudao.module.statistics.service.product.ProductStatisticsService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 商品统计")
@RestController
@RequestMapping("/statistics/product")
@Validated
@Slf4j
public class ProductStatisticsController {
    @Resource
    private ProductStatisticsService productStatisticsService;

    // TODO @麦子：返回 ProductStatisticsComparisonResp， 里面有两个字段，一个是选择的时间范围的合计结果，一个是对比的时间范围的合计结果；
    // 例如说，选择时间范围是 2023-10-01 ~ 2023-10-02，那么对比就是 2023-09-30，再倒推 2 天；
    public CommonResult<Object> getProductStatisticsComparison() {
        return null;
    }

    // TODO @麦子：查询指定时间范围内的商品统计数据；DO 到时需要改成 VO 哈
    public CommonResult<List<ProductStatisticsDO>> getProductStatisticsList(
            LocalDateTime[] times) {
        return null;
    }

    // TODO @麦子：查询指定时间范围内的商品 SPU 统计数据；DO 到时需要改成 VO 哈
    // 入参是分页参数 + 时间范围 + 排序字段
    public CommonResult<PageResult<ProductSpuStatisticsDO>> getProductSpuStatisticsPage() {
        return null;
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品统计分页")
    @PreAuthorize("@ss.hasPermission('statistics:product:query')")
    public CommonResult<PageResult<ProductRespVO>> getProductPage(@Valid ProductPageReqVO pageReqVO) {
        return success(productStatisticsService.getProductPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出商品统计 Excel")
    @PreAuthorize("@ss.hasPermission('statistics:product:export')")
    public void exportProductExcel(ProductPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductRespVO> list = productStatisticsService.getProductPage(pageReqVO).getList();
        List<ProductExcelRespVO> resList = new ArrayList<>();
        if (list!=null && list.size()>0){
            for(ProductRespVO dd:list){
                ProductExcelRespVO respVO = BeanUtils.toBean(dd,ProductExcelRespVO.class);
                respVO.setDeliveryTime(formatTime2(dd.getDeliveryTime()));
                resList.add(respVO);
            }
        }
        ExcelUtils.write(response, "商品汇总.xls", "统计", ProductExcelRespVO.class, resList);
    }

    @GetMapping("/detail/page")
    @Operation(summary = "获得商品明细统计分页")
    @PreAuthorize("@ss.hasPermission('statistics:productDetail:query')")
    public CommonResult<PageResult<ProductDetailRespVO>> getProductDetailPage(@Valid ProductPageReqVO pageReqVO) {
        return success(productStatisticsService.getProductDetailPage(pageReqVO));
    }

    @GetMapping("/detail/export-excel")
    @Operation(summary = "导出商品明细统计 Excel")
    @PreAuthorize("@ss.hasPermission('statistics:productDetail:export')")
    public void exportProductDetailExcel(ProductPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductDetailRespVO> list = productStatisticsService.getProductDetailPage(pageReqVO).getList();
        List<ProductDetailExcelRespVO> resList = new ArrayList<>();
        if (list!=null && list.size()>0){
            for(ProductDetailRespVO dd:list){
                ProductDetailExcelRespVO respVO = BeanUtils.toBean(dd,ProductDetailExcelRespVO.class);
                respVO.setDeliveryTime(formatTime2(dd.getDeliveryTime()));
                resList.add(respVO);
            }
        }
        ExcelUtils.write(response, "商品明细.xls", "统计", ProductDetailExcelRespVO.class, resList);
    }

    public String formatTime2(Integer data){
        String result = "";
        if (data!=null){
            int day = data/(24 * 60 * 60 * 1000);
            int hour = data/(60 * 60 * 1000) - day * 24;
            int minute = data/ (60 * 1000) - day * 24 * 60 - hour * 60;
            int second = data/1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60;
            if (day > 0) {
                return day + "天" + hour + "小时" + minute + "分钟";
            }
            if (hour > 0) {
                return hour + "小时" + minute + "分钟";
            }
            if (minute > 0) {
                return minute + "分钟";
            }
            if (second > 0) {
                return second + "秒";
            } else {
                return 0 + "秒";
            }
        }
        return result;
    }

}
