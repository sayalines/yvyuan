package cn.iocoder.yudao.module.member.controller.admin.exchangeConfig;

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

import cn.iocoder.yudao.module.member.controller.admin.exchangeConfig.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeConfig.ExchangeConfigDO;
import cn.iocoder.yudao.module.member.service.exchangeConfig.ExchangeConfigService;

@Tag(name = "管理后台 - 兑换配置")
@RestController
@RequestMapping("/member/exchange-config")
@Validated
public class ExchangeConfigController {

    @Resource
    private ExchangeConfigService exchangeConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建兑换配置")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:create')")
    public CommonResult<Long> createExchangeConfig(@Valid @RequestBody ExchangeConfigSaveReqVO createReqVO) {
        return success(exchangeConfigService.createExchangeConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新兑换配置")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:update')")
    public CommonResult<Boolean> updateExchangeConfig(@Valid @RequestBody ExchangeConfigSaveReqVO updateReqVO) {
        exchangeConfigService.updateExchangeConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除兑换配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:exchange-config:delete')")
    public CommonResult<Boolean> deleteExchangeConfig(@RequestParam("id") Long id) {
        exchangeConfigService.deleteExchangeConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得兑换配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:query')")
    public CommonResult<ExchangeConfigRespVO> getExchangeConfig(@RequestParam("id") Long id) {
        ExchangeConfigDO exchangeConfig = exchangeConfigService.getExchangeConfig(id);
        return success(BeanUtils.toBean(exchangeConfig, ExchangeConfigRespVO.class));
    }

    @PostMapping("/create-exchange")
    @Operation(summary = "生成兑换码")
    public CommonResult<Boolean> createExchange(Long configId,Integer count) {
        exchangeConfigService.createExchange(configId,count);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得兑换配置分页")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:query')")
    public CommonResult<PageResult<ExchangeConfigRespVO>> getExchangeConfigPage(@Valid ExchangeConfigPageReqVO pageReqVO) {
        PageResult<ExchangeConfigDO> pageResult = exchangeConfigService.getExchangeConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ExchangeConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出兑换配置 Excel")
    @PreAuthorize("@ss.hasPermission('member:exchange-config:export')")
    @OperateLog(type = EXPORT)
    public void exportExchangeConfigExcel(@Valid ExchangeConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExchangeConfigDO> list = exchangeConfigService.getExchangeConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "兑换配置.xls", "数据", ExchangeConfigRespVO.class,
                        BeanUtils.toBean(list, ExchangeConfigRespVO.class));
    }

}