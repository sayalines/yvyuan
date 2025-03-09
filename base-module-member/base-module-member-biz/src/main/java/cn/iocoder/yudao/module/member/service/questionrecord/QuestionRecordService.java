package cn.iocoder.yudao.module.member.service.questionrecord;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.questionrecord.QuestionRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.apache.ibatis.annotations.Param;

/**
 * 问卷记录 Service 接口
 *
 * @author 超级管理员
 */
public interface QuestionRecordService {

    /**
     * 创建问卷记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQuestionRecord(@Valid QuestionRecordSaveReqVO createReqVO);

    /**
     * 更新问卷记录
     *
     * @param updateReqVO 更新信息
     */
    void updateQuestionRecord(@Valid QuestionRecordSaveReqVO updateReqVO);

    /**
     * 删除问卷记录
     *
     * @param id 编号
     */
    void deleteQuestionRecord(Long id);

    /**
     * 获得问卷记录
     *
     * @param id 编号
     * @return 问卷记录
     */
    QuestionRecordDO getQuestionRecord(Long id);

    /**
     * 获得问卷记录分页
     *
     * @param pageReqVO 分页查询
     * @return 问卷记录分页
     */
    PageResult<QuestionRecordDO> getQuestionRecordPage(QuestionRecordPageReqVO pageReqVO);
    /**
     * 按年各维度重点关注人数对比
     * @return
     */
    List<Map<String,Object>> findListByTime(LocalDateTime beginTime,LocalDateTime endTime);
    /**
     * 四个指定量表的测评结果各维度的人数占比情况
     * @return
     */
    List<Map<String,Object>> findListByQuestionId(LocalDateTime beginTime,LocalDateTime endTime);
}