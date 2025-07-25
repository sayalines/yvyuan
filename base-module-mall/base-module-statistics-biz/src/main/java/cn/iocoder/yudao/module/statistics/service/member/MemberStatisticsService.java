package cn.iocoder.yudao.module.statistics.service.member;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.DataComparisonRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.member.vo.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员信息的统计 Service 接口
 *
 * @author owen
 */
public interface MemberStatisticsService {

    // TODO 芋艿：已经 review
    /**
     * 获取会员统计（实时统计）
     *
     * @return 会员统计
     */
    MemberSummaryRespVO getMemberSummary();

    // TODO 芋艿：已经 review
    /**
     * 获取会员分析对照数据
     *
     * @param beginTime 起始时间
     * @param endTime   截止时间
     * @return 会员分析对照数据
     */
    DataComparisonRespVO<MemberAnalyseDataRespVO> getMemberAnalyseComparisonData(LocalDateTime beginTime,
                                                                                 LocalDateTime endTime);

    // TODO 芋艿：已经 review
    /**
     * 按照省份，获得会员统计列表
     *
     * @return 会员统计列表
     */
    List<MemberAreaStatisticsRespVO> getMemberAreaStatisticsList();

    // TODO 芋艿：已经 review
    /**
     * 按照性别，获得会员统计列表
     *
     * @return 会员统计列表
     */
    List<MemberSexStatisticsRespVO> getMemberSexStatisticsList();

    /**
     * 按照终端，获得会员统计列表
     *
     * @return 会员统计列表
     */
    List<MemberTerminalStatisticsRespVO> getMemberTerminalStatisticsList();

    // TODO 芋艿：已经 review
    /**
     * 获取用户注册数量列表
     *
     * @param beginTime 起始时间
     * @param endTime   截止时间
     * @return 注册数量列表
     */
    List<MemberRegisterCountRespVO> getMemberRegisterCountList(LocalDateTime beginTime, LocalDateTime endTime);

    // TODO 芋艿：已经 review
    /**
     * 获得用户数量量统计对照
     *
     * @return 用户数量量统计对照
     */
    DataComparisonRespVO<MemberCountRespVO> getUserCountComparison();

    /**
     *获得访客信息分页
     * @param pageReqVO
     * @return
     */
    PageResult<MemberVisitRespVO> getMemberVisitPage(MemberVisitPageReqVO pageReqVO);
    /**
     *获得访客行为分页
     * @param pageReqVO
     * @return
     */
    PageResult<MemberActionRespVO> getMemberActionPage(MemberActionPageReqVO pageReqVO);

}
