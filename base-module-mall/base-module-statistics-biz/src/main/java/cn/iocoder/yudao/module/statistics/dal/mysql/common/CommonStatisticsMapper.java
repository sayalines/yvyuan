package cn.iocoder.yudao.module.statistics.dal.mysql.common;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;

/**
 * 常用统计 Mapper
 *
 * @author owen
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface CommonStatisticsMapper extends BaseMapperX {
    /**
     * 获取单品上新订阅数
     * @return
     */
    Long selectProductSubscribeCountByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,@Param("endTime") LocalDateTime endTime);
    /**
     * 获取活动开始订阅数
     * @return
     */
    Long selectActiveSubscribeCountByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,@Param("endTime") LocalDateTime endTime);
    /**
     * 获取活动参与人数
     * @return
     */
    Long selectActiveUserCountByCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,@Param("endTime") LocalDateTime endTime);
}
