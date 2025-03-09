package cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.admin.exchangeConfigItem.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfigItem.ExchangeConfigItemDO;
import cn.iocoder.yudao.module.member.service.exchangeConfigItem.ExchangeConfigItemService;

@Tag(name = "管理后台 - 兑换配置明细")
@RestController
@RequestMapping("/member/exchange-config-item")
@Validated
public class ExchangeConfigItemController {

    @Resource
    private ExchangeConfigItemService exchangeConfigItemService;

    @PostMapping("/create")
    @Operation(summary = "创建兑换配置明细")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:create')")
    public CommonResult<Long> createExchangeConfigItem(@Valid @RequestBody ExchangeConfigItemSaveReqVO createReqVO) {
        return success(exchangeConfigItemService.createExchangeConfigItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新兑换配置明细")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:update')")
    public CommonResult<Boolean> updateExchangeConfigItem(@Valid @RequestBody ExchangeConfigItemSaveReqVO updateReqVO) {
        exchangeConfigItemService.updateExchangeConfigItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除兑换配置明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:exchange-config:delete')")
    public CommonResult<Boolean> deleteExchangeConfigItem(@RequestParam("id") Long id) {
        exchangeConfigItemService.deleteExchangeConfigItem(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得兑换配置明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:query')")
    public CommonResult<ExchangeConfigItemRespVO> getExchangeConfigItem(@RequestParam("id") Long id) {
        ExchangeConfigItemDO exchangeConfigItem = exchangeConfigItemService.getExchangeConfigItem(id);
        return success(BeanUtils.toBean(exchangeConfigItem, ExchangeConfigItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得兑换配置明细分页")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:query')")
    public CommonResult<PageResult<ExchangeConfigItemRespVO>> getExchangeConfigItemPage(@Valid ExchangeConfigItemPageReqVO pageReqVO) {
        PageResult<ExchangeConfigItemDO> pageResult = exchangeConfigItemService.getExchangeConfigItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ExchangeConfigItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出兑换配置明细 Excel")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:export')")
    @OperateLog(type = EXPORT)
    public void exportExchangeConfigItemExcel(@Valid ExchangeConfigItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExchangeConfigItemDO> list = exchangeConfigItemService.getExchangeConfigItemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "兑换明细.xls", "数据", ExchangeConfigItemRespVO.class,
                        BeanUtils.toBean(list, ExchangeConfigItemRespVO.class));
    }

}