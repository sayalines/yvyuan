package cn.iocoder.yudao.module.member.dal.mysql.log;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.membervisitlog.vo.MemberVisitLogPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.membervisitlog.MemberVisitLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 日志 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface LogMapper {
    /**
     * 获取统计时间
     * @return
     */
    List<String> getDateList();

    /**
     * 获取访客信息数
     * @param beginTime
     * @param endTime
     * @return
     */
    Long getMemberVisitCount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取访客信息数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getMemberVisitList(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取访客行为数
     * @param beginTime
     * @param endTime
     * @return
     */
    Long getMemberActionCount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取访客行为数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getMemberActionList(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取访客点击数
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getMemberActionHitList(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取产品访问数
     * @param beginTime
     * @param endTime
     * @return
     */
    Long getProductVisitCount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
    /**
     * 获取产品访问数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getProductVisitList(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取产品购物车数据
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getProductCartList(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
}