package cn.iocoder.yudao.module.member.service.productvisitlog;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.productvisitlog.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.productvisitlog.ProductVisitLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 商品访问日志 Service 接口
 *
 * @author 超级管理员
 */
public interface ProductVisitLogService {

    /**
     * 创建商品访问日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductVisitLog(@Valid ProductVisitLogSaveReqVO createReqVO);

    /**
     * 更新商品访问日志
     *
     * @param updateReqVO 更新信息
     */
    void updateProductVisitLog(@Valid ProductVisitLogSaveReqVO updateReqVO);

    /**
     * 删除商品访问日志
     *
     * @param id 编号
     */
    void deleteProductVisitLog(Long id);

    /**
     * 获得商品访问日志
     *
     * @param id 编号
     * @return 商品访问日志
     */
    ProductVisitLogDO getProductVisitLog(Long id);

    /**
     * 获得商品访问日志分页
     *
     * @param pageReqVO 分页查询
     * @return 商品访问日志分页
     */
    PageResult<ProductVisitLogDO> getProductVisitLogPage(ProductVisitLogPageReqVO pageReqVO);

}