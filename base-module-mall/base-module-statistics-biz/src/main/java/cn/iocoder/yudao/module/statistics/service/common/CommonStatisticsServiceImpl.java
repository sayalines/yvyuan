package cn.iocoder.yudao.module.statistics.service.common;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.CommonPageReqVO;
import cn.iocoder.yudao.module.statistics.controller.admin.common.vo.OtherRespVO;
import cn.iocoder.yudao.module.statistics.dal.mysql.common.CommonStatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 常用统计
 */
@Service
@Validated
public class CommonStatisticsServiceImpl implements CommonStatisticsService {

    @Resource
    private CommonStatisticsMapper commonStatisticsMapper;

    @Override
    public PageResult<OtherRespVO> getOtherage(CommonPageReqVO pageReqVO) {
        PageResult<OtherRespVO> pageResult = new PageResult<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (pageReqVO.getCreateTime()!=null && pageReqVO.getCreateTime().length==2){
            startTime = pageReqVO.getCreateTime()[0];
            endTime = pageReqVO.getCreateTime()[1];
        }
        Long total = 1L;
        List<OtherRespVO> list = new ArrayList<>();
        //获取其他统计
        OtherRespVO respVO = new OtherRespVO();
        Long productSubscribeCount = commonStatisticsMapper.selectProductSubscribeCountByCreateTimeBetween(startTime, endTime);
        Long activeSubscribeCount = commonStatisticsMapper.selectActiveSubscribeCountByCreateTimeBetween(startTime, endTime);
        Long activeUserCount = commonStatisticsMapper.selectActiveUserCountByCreateTimeBetween(startTime, endTime);
        if (productSubscribeCount==null){
            productSubscribeCount=0L;
        }
        if (activeSubscribeCount==null){
            activeSubscribeCount=0L;
        }
        if (activeUserCount==null){
            activeUserCount=0L;
        }
        respVO.setProductSubscribeCount(productSubscribeCount);
        respVO.setActiveSubscribeCount(activeSubscribeCount);
        respVO.setActiveUserCount(activeUserCount);
        list.add(respVO);

        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }
}
