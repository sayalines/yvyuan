package cn.iocoder.yudao.module.system.service.questiontopic;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.questiontopic.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questiontopic.QuestionTopicDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 量表题目 Service 接口
 *
 * @author 超级管理员
 */
public interface QuestionTopicService {

    /**
     * 创建量表题目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createQuestionTopic(@Valid QuestionTopicSaveReqVO createReqVO);

    /**
     * 更新量表题目
     *
     * @param updateReqVO 更新信息
     */
    void updateQuestionTopic(@Valid QuestionTopicSaveReqVO updateReqVO);

    /**
     * 删除量表题目
     *
     * @param id 编号
     */
    void deleteQuestionTopic(Long id);

    /**
     * 复制量表题目
     *
     * @param id 编号
     */
    void copyQuestionTopic(Long id);

    /**
     * 获得量表题目
     *
     * @param id 编号
     * @return 量表题目
     */
    QuestionTopicDO getQuestionTopic(Long id);

    /**
     * 获得量表题目分页
     *
     * @param pageReqVO 分页查询
     * @return 量表题目分页
     */
    PageResult<QuestionTopicDO> getQuestionTopicPage(QuestionTopicPageReqVO pageReqVO);
    /**
     * 获得量表题目
     *
     * @return 量表题目
     */
    QuestionTopicDO getQuestionTopic(Long questionId,String topicCode,Integer currentNo,String orderBy);

    /**
     * 获取题目总数
     * @param questionId
     * @return
     */
    Long getTotalCount(Long questionId);

    /**
     * 获取题目列表
     * @param ids
     * @return
     */
    List<QuestionTopicDO> getQuestionTopicList(Collection<Long> ids);

    /**
     * 批量导入
     * @param file  excel文件
     * @param id  问卷ID
     */
    void importExcel(MultipartFile file, Long id, Boolean updateSupport);
    /**
     * 获取题目列表
     * @param questionId
     * @return
     */
    List<QuestionTopicDO> getQuestionTopicListById(Long questionId);

}