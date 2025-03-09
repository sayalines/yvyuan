package cn.iocoder.yudao.module.trade.dal.mysql.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.TradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.controller.app.order.vo.AppTradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderStatusEnum;
import cn.iocoder.yudao.module.trade.enums.order.TradeOrderTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface TradeOrderMapper extends BaseMapperX<TradeOrderDO> {

    default int updateByIdAndStatus(Long id, Integer status, TradeOrderDO update) {
        return update(update, new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getId, id).eq(TradeOrderDO::getStatus, status));
    }

    default TradeOrderDO selectByIdAndUserId(Long id, Long userId) {
        return selectOne(TradeOrderDO::getId, id, TradeOrderDO::getUserId, userId);
    }

    default PageResult<TradeOrderDO> selectPage(TradeOrderPageReqVO reqVO, Set<Long> userIds) {
        LambdaQueryWrapper<TradeOrderDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(reqVO.getNo())){
            wrapper = wrapper.like(TradeOrderDO::getNo, reqVO.getNo());
        }
        if (reqVO.getId()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getId, reqVO.getId());
        }
        if (reqVO.getUserId()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getUserId, reqVO.getUserId());
        }
        if (userIds!=null && userIds.size()>0){
            wrapper = wrapper.in(TradeOrderDO::getUserId, userIds);
        }
        if (reqVO.getType()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getType, reqVO.getType());
        }
        if (reqVO.getStatus()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getStatus, reqVO.getStatus());
        }
        if (reqVO.getPayStatus()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getPayStatus, reqVO.getPayStatus());
        }
        if (StringUtils.isNotEmpty(reqVO.getPayChannelCode())){
            wrapper = wrapper.eq(TradeOrderDO::getPayChannelCode, reqVO.getPayChannelCode());
        }
        if (reqVO.getTerminal()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getTerminal, reqVO.getTerminal());
        }
        if (reqVO.getLogisticsId()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getLogisticsId, reqVO.getLogisticsId());
        }
        if (reqVO.getPickUpStoreIds()!=null && reqVO.getPickUpStoreIds().size()>0){
            wrapper = wrapper.in(TradeOrderDO::getPickUpStoreId, reqVO.getPickUpStoreIds());
        }
        if (reqVO.getCreateTime()!=null){
            wrapper = wrapper.between(TradeOrderDO::getCreateTime, reqVO.getCreateTime()[0],reqVO.getCreateTime()[1]);
        }
        if (StringUtils.isNotEmpty(reqVO.getReceiverName())){
            wrapper = wrapper.eq(TradeOrderDO::getReceiverName, reqVO.getReceiverName());
        }
        if (StringUtils.isNotEmpty(reqVO.getReceiverMobile())){
            wrapper = wrapper.eq(TradeOrderDO::getReceiverMobile, reqVO.getReceiverMobile());
        }

        wrapper = wrapper.orderByDesc(TradeOrderDO::getId);
        return selectPage(reqVO,wrapper);
    }

    // TODO @疯狂：如果用 map 返回，要不这里直接用 TradeOrderSummaryRespVO 返回？也算合理，就当  sql 查询出这么个玩意~~
    default List<Map<String, Object>> selectOrderSummaryGroupByRefundStatus(TradeOrderPageReqVO reqVO, Set<Long> userIds) {
        return selectMaps(new MPJLambdaWrapperX<TradeOrderDO>()
                .selectAs(TradeOrderDO::getRefundStatus, TradeOrderDO::getRefundStatus)  // 售后状态
                .selectCount(TradeOrderDO::getId, "count") // 售后状态对应的数量
                .selectSum(TradeOrderDO::getPayPrice, "price")  // 售后状态对应的支付金额
                .likeIfPresent(TradeOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(TradeOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(TradeOrderDO::getDeliveryType, reqVO.getDeliveryType())
                .inIfPresent(TradeOrderDO::getUserId, userIds)
                .eqIfPresent(TradeOrderDO::getType, reqVO.getType())
                .eqIfPresent(TradeOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TradeOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .eqIfPresent(TradeOrderDO::getTerminal, reqVO.getTerminal())
                .eqIfPresent(TradeOrderDO::getLogisticsId, reqVO.getLogisticsId())
                .inIfPresent(TradeOrderDO::getPickUpStoreId, reqVO.getPickUpStoreIds())
                .likeIfPresent(TradeOrderDO::getPickUpVerifyCode, reqVO.getPickUpVerifyCode())
                .betweenIfPresent(TradeOrderDO::getCreateTime, reqVO.getCreateTime())
                .groupBy(TradeOrderDO::getRefundStatus)); // 按售后状态分组
    }

    default PageResult<TradeOrderDO> selectPage(AppTradeOrderPageReqVO reqVO, Long userId) {
        LambdaQueryWrapper<TradeOrderDO> wrapper = new LambdaQueryWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId);
        if (reqVO.getStatus()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getStatus, reqVO.getStatus());
        }

        if (reqVO.getCommentStatus()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getCommentStatus, reqVO.getCommentStatus());
        }

        wrapper = wrapper.orderByDesc(TradeOrderDO::getId);
        return selectPage(reqVO, wrapper); // TODO 芋艿：未来不同的 status，不同的排序
    }

    default Long selectCount(AppTradeOrderPageReqVO reqVO, Long userId) {
        LambdaQueryWrapper<TradeOrderDO> wrapper = new LambdaQueryWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId);
        if (reqVO.getStatus()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getStatus, reqVO.getStatus());
        }

        if (reqVO.getCommentStatus()!=null){
            wrapper = wrapper.eq(TradeOrderDO::getCommentStatus, reqVO.getCommentStatus());
        }

        return selectCount(wrapper);
    }

    default Long selectCountByUserIdAndStatus(Long userId, Integer status, Boolean commentStatus) {
        return selectCount(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId)
                .eqIfPresent(TradeOrderDO::getStatus, status)
                .eqIfPresent(TradeOrderDO::getCommentStatus, commentStatus));
    }

    default Long selectCountByUserIdAndStatus(Long userId, Integer status, Boolean commentStatus,Integer notType) {
        return selectCount(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId)
                .neIfPresent(TradeOrderDO::getType, notType)
                .eqIfPresent(TradeOrderDO::getStatus, status)
                .eqIfPresent(TradeOrderDO::getCommentStatus, commentStatus));
    }

    default TradeOrderDO selectOrderByIdAndUserId(Long orderId, Long loginUserId) {
        return selectOne(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getId, orderId)
                .eq(TradeOrderDO::getUserId, loginUserId));
    }

    default List<TradeOrderDO> selectListByStatusAndCreateTimeLt(Integer status, LocalDateTime createTime) {
        return selectList(new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getStatus, status)
                .lt(TradeOrderDO::getCreateTime, createTime));
    }

    default List<TradeOrderDO> selectListByStatusAndDeliveryTimeLt(Integer status, LocalDateTime deliveryTime) {
        return selectList(new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getStatus, status)
                .lt(TradeOrderDO::getDeliveryTime, deliveryTime));
    }

    default List<TradeOrderDO> selectListByStatusAndReceiveTimeLt(Integer status, LocalDateTime receive,
                                                                  Boolean commentStatus) {
        return selectList(new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getStatus, status)
                .lt(TradeOrderDO::getReceiveTime, receive)
                .eq(TradeOrderDO::getCommentStatus, commentStatus));
    }

    default List<TradeOrderDO> selectListByUserIdAndSeckillActivityId(Long userId, Long seckillActivityId) {
        return selectList(new LambdaUpdateWrapper<>(TradeOrderDO.class)
                .eq(TradeOrderDO::getUserId, userId)
                .eq(TradeOrderDO::getSeckillActivityId, seckillActivityId));
    }

    default TradeOrderDO selectOneByPickUpVerifyCode(String pickUpVerifyCode) {
        return selectOne(TradeOrderDO::getPickUpVerifyCode, pickUpVerifyCode);
    }

    default TradeOrderDO selectByUserIdAndCombinationActivityIdAndStatus(Long userId, Long combinationActivityId, Integer status) {
        return selectOne(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId)
                .eq(TradeOrderDO::getStatus, status)
                .eq(TradeOrderDO::getCombinationActivityId, combinationActivityId)
        );
    }

    default List<TradeOrderDO> selectListByStatusAndBox(Long userId, Integer status, Boolean isBox) {
        return selectList(new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId)
                .eq(TradeOrderDO::getStatus, status)
        );
    }

    @Select("SELECT SUM(b.count) as totalCount " +
            "from trade_order a " +
            "INNER JOIN trade_order_item b on a.id=b.order_id " +
            "INNER JOIN product_spu c on b.spu_id=c.id " +
            "where a.user_id=#{userId} and b.spu_id=#{spuId} and a.pay_status=1 and c.limit_count>0 ")
    Integer findPayProductCountByUserIdAndSpuId(@Param("userId") Long userId,@Param("spuId") Long spuId);

    //查看是否存在已支付，但订单为已取消的订单
    List<TradeOrderDO> selectPayCancelOrderList();
}
