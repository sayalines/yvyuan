package cn.iocoder.yudao.module.product.controller.admin.spu;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.yudao.module.product.controller.admin.category.vo.ProductCategoryRespVO;
import cn.iocoder.yudao.module.product.controller.admin.spu.vo.*;
import cn.iocoder.yudao.module.product.convert.category.ProductCategoryConvert;
import cn.iocoder.yudao.module.product.convert.spu.ProductSpuConvert;
import cn.iocoder.yudao.module.product.dal.dataobject.category.ProductCategoryDO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.module.product.enums.spu.ProductSpuStatusEnum;
import cn.iocoder.yudao.module.product.service.category.ProductCategoryService;
import cn.iocoder.yudao.module.product.service.sku.ProductSkuService;
import cn.iocoder.yudao.module.product.service.spu.ProductSpuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.module.product.enums.ErrorCodeConstants.SPU_NOT_EXISTS;

@Tag(name = "管理后台 - 商品 SPU")
@RestController
@RequestMapping("/product/spu")
@Validated
public class ProductSpuController {

    @Resource
    private ProductSpuService productSpuService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ProductCategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建商品 SPU")
    @PreAuthorize("@ss.hasPermission('product:spu:create')")
    public CommonResult<Long> createProductSpu(@Valid @RequestBody ProductSpuCreateReqVO createReqVO) {
        return success(productSpuService.createSpu(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品 SPU")
    @PreAuthorize("@ss.hasPermission('product:spu:update')")
    public CommonResult<Boolean> updateSpu(@Valid @RequestBody ProductSpuUpdateReqVO updateReqVO) {
        productSpuService.updateSpu(updateReqVO);
        return success(true);
    }

    @PutMapping("/valid-sku-for-delete")
    @Operation(summary = "删除规格前校验")
    @PreAuthorize("@ss.hasPermission('product:spu:update')")
    public CommonResult<Boolean> validSkuBeforeDelete(@RequestParam("id") Long id) {
        productSpuService.validSkuBeforeDelete(id);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新商品 SPU Status")
    @PreAuthorize("@ss.hasPermission('product:spu:update')")
    public CommonResult<Boolean> updateStatus(@Valid @RequestBody ProductSpuUpdateStatusReqVO updateReqVO) {
        productSpuService.updateSpuStatus(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品 SPU")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('product:spu:delete')")
    public CommonResult<Boolean> deleteSpu(@RequestParam("id") Long id) {
        productSpuService.deleteSpu(id);
        return success(true);
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得商品 SPU 明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('product:spu:query')")
    public CommonResult<ProductSpuDetailRespVO> getSpuDetail(@RequestParam("id") Long id) {
        // 获得商品 SPU
        ProductSpuDO spu = productSpuService.getSpu(id);
        if (spu == null) {
            throw exception(SPU_NOT_EXISTS);
        }
        // 查询商品 SKU
        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(spu.getId());
        return success(ProductSpuConvert.INSTANCE.convertForSpuDetailRespVO(spu, skus));
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得商品 SPU 精简列表")
    @PreAuthorize("@ss.hasPermission('product:spu:query')")
    public CommonResult<List<ProductSpuSimpleRespVO>> getSpuSimpleList() {
        List<ProductSpuDO> list = productSpuService.getSpuListByStatus(ProductSpuStatusEnum.ENABLE.getStatus());
        // 降序排序后，返回给前端
        list.sort(Comparator.comparing(ProductSpuDO::getSort).reversed());
        return success(ProductSpuConvert.INSTANCE.convertList02(list));
    }

    @GetMapping("/list")
    @Operation(summary = "获得商品 SPU 详情列表")
    @Parameter(name = "spuIds", description = "spu 编号列表", required = true, example = "[1,2,3]")
    @PreAuthorize("@ss.hasPermission('product:spu:query')")
    public CommonResult<List<ProductSpuDetailRespVO>> getSpuList(@RequestParam("spuIds") Collection<Long> spuIds) {
        return success(ProductSpuConvert.INSTANCE.convertForSpuDetailRespListVO(
                productSpuService.getSpuList(spuIds), productSkuService.getSkuListBySpuId(spuIds)));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品 SPU 分页")
    @PreAuthorize("@ss.hasPermission('product:spu:query')")
    public CommonResult<PageResult<ProductSpuRespVO>> getSpuPage(@Valid ProductSpuPageReqVO pageVO) {
        return success(ProductSpuConvert.INSTANCE.convertPage(productSpuService.getSpuPage(pageVO)));
    }

    @GetMapping("/get-count")
    @Operation(summary = "获得商品 SPU 分页 tab count")
    @PreAuthorize("@ss.hasPermission('product:spu:query')")
    public CommonResult<Map<Integer, Long>> getSpuCount() {
        return success(productSpuService.getTabsCount());
    }

    @GetMapping("/export")
    @Operation(summary = "导出商品")
    @PreAuthorize("@ss.hasPermission('product:spu:export')")
    @OperateLog(type = EXPORT)
    public void exportUserList(@Validated ProductSpuExportReqVO reqVO,
                               HttpServletResponse response) throws IOException {
        List<ProductSpuDO> spuList = productSpuService.getSpuList(reqVO);
        // 导出 Excel
        List<ProductSpuExcelVO> datas = ProductSpuConvert.INSTANCE.convertList03(spuList);
        ExcelUtils.write(response, "商品列表.xls", "数据", ProductSpuExcelVO.class, datas);
    }


    @GetMapping("/tree")
    @Operation(summary = "获得商品树")
    @PreAuthorize("@ss.hasPermission('product:spu:query')")
    public CommonResult<List<ProductTreeVO>> getTree(Long categoryId,String filterIds) {
        ProductSpuExportReqVO reqVO = new ProductSpuExportReqVO();
        if (categoryId!=null){
            reqVO.setCategoryId(categoryId);
        }
        List<String> filterList = new ArrayList<>();
        if (StringUtils.isNotEmpty(filterIds)){
            for(String ss:filterIds.split(",")){
                if (StringUtils.isNotEmpty(ss)){
                    if (filterList.indexOf(ss)==-1){
                        filterList.add(ss);
                    }
                }
            }
        }
        Long currentTime = new Date().getTime();
        List<ProductTreeVO> resultList = new ArrayList<>();
        List<ProductSpuDO> spuList = productSpuService.getSpuList(reqVO);
        if (spuList!=null && spuList.size()>0){
            for(ProductSpuDO dd:spuList){
                if (filterList.indexOf(dd.getId().toString())==-1){
                    ProductTreeVO treeVO = new ProductTreeVO();
                    treeVO.setId(dd.getId());
                    treeVO.setParentId(currentTime+dd.getCategoryId());
                    treeVO.setName(dd.getName());
                    treeVO.setLeaf(true);
                    resultList.add(treeVO);
                }
            }

            List<ProductCategoryDO> categoryList = categoryService.getEnableCategoryList(new ProductCategoryListReqVO());
            if (categoryList!=null && categoryList.size()>0){
                for(ProductCategoryDO dd:categoryList){
                    ProductTreeVO treeVO = new ProductTreeVO();
                    treeVO.setId(currentTime+dd.getId());
                    treeVO.setName(dd.getName());
                    if (dd.getParentId().compareTo(0L)==0){
                        treeVO.setParentId(dd.getParentId());
                    }else{
                        treeVO.setParentId(currentTime+dd.getParentId());
                    }
                    treeVO.setLeaf(false);
                    resultList.add(treeVO);
                }
            }

        }



        return success(resultList);
    }

    @GetMapping("/sku-list")
    @Operation(summary = "获得商品规格列表")
    public CommonResult<List<ProductSkuVO>> getSkuList(Long id) {
        List<ProductSkuVO> resultList = new ArrayList<>();
        List<ProductSkuDO> skuList  = productSkuService.getSkuListBySpuId(id);
        if (skuList!=null && skuList.size()>0){
            for(ProductSkuDO dd:skuList){
                String name = "默认";
                List<ProductSkuDO.Property> properties = dd.getProperties();
                if (properties!=null && properties.size()>0){
                    ProductSkuDO.Property property = properties.get(0);
                    name = property.getValueName();
                }
                ProductSkuVO sku = new ProductSkuVO();
                sku.setId(dd.getId());
                sku.setName(name);
                resultList.add(sku);
            }
        }
        return success(resultList);
    }

}
