package cn.iocoder.yudao.module.member.controller.admin.exchangeRecord;

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

import cn.iocoder.yudao.module.member.controller.admin.exchangeRecord.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.exchangeRecord.ExchangeRecordDO;
import cn.iocoder.yudao.module.member.service.exchangeRecord.ExchangeRecordService;

@Tag(name = "管理后台 - 会员兑换记录")
@RestController
@RequestMapping("/member/exchange-record")
@Validated
public class ExchangeRecordController {

    @Resource
    private ExchangeRecordService exchangeRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建会员兑换记录")
    @PreAuthorize("@ss.hasPermission('member:exchange-record:create')")
    public CommonResult<Long> createExchangeRecord(@Valid @RequestBody ExchangeRecordSaveReqVO createReqVO) {
        return success(exchangeRecordService.createExchangeRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员兑换记录")
    @PreAuthorize("@ss.hasPermission('member:exchange-record:update')")
    public CommonResult<Boolean> updateExchangeRecord(@Valid @RequestBody ExchangeRecordSaveReqVO updateReqVO) {
        exchangeRecordService.updateExchangeRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员兑换记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:exchange-record:delete')")
    public CommonResult<Boolean> deleteExchangeRecord(@RequestParam("id") Long id) {
        exchangeRecordService.deleteExchangeRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员兑换记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:exchange-record:query')")
    public CommonResult<ExchangeRecordRespVO> getExchangeRecord(@RequestParam("id") Long id) {
        ExchangeRecordDO exchangeRecord = exchangeRecordService.getExchangeRecord(id);
        return success(BeanUtils.toBean(exchangeRecord, ExchangeRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员兑换记录分页")
    @PreAuthorize("@ss.hasPermission('member:exchange-record:query')")
    public CommonResult<PageResult<ExchangeRecordRespVO>> getExchangeRecordPage(@Valid ExchangeRecordPageReqVO pageReqVO) {
        return success(exchangeRecordService.getExchangeRecordPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员兑换记录 Excel")
    @PreAuthorize("@ss.hasPermission('member:exchange-record:export')")
    @OperateLog(type = EXPORT)
    public void exportExchangeRecordExcel(@Valid ExchangeRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExchangeRecordRespVO> list = exchangeRecordService.getExchangeRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员兑换记录.xls", "数据", ExchangeRecordRespVO.class,
                        BeanUtils.toBean(list, ExchangeRecordRespVO.class));
    }

}