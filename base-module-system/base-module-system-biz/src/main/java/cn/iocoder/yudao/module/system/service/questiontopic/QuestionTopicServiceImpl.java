package cn.iocoder.yudao.module.system.service.questiontopic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jodd.util.StringUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.system.controller.admin.questiontopic.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.questiontopic.QuestionTopicDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.system.dal.mysql.questiontopic.QuestionTopicMapper;
import org.springframework.web.multipart.MultipartFile;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 量表题目 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class QuestionTopicServiceImpl implements QuestionTopicService {

    @Resource
    private QuestionTopicMapper questionTopicMapper;

    @Override
    public Long createQuestionTopic(QuestionTopicSaveReqVO createReqVO) {
        // 插入
        QuestionTopicDO questionTopic = BeanUtils.toBean(createReqVO, QuestionTopicDO.class);
        questionTopicMapper.insert(questionTopic);
        // 返回
        return questionTopic.getId();
    }

    @Override
    public void updateQuestionTopic(QuestionTopicSaveReqVO updateReqVO) {
        // 校验存在
        validateQuestionTopicExists(updateReqVO.getId());
        // 更新
        QuestionTopicDO updateObj = BeanUtils.toBean(updateReqVO, QuestionTopicDO.class);
        questionTopicMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestionTopic(Long id) {
        // 校验存在
        validateQuestionTopicExists(id);
        // 删除
        questionTopicMapper.deleteById(id);
    }

    @Override
    public void copyQuestionTopic(Long id) {
        QuestionTopicDO topicDO = questionTopicMapper.selectById(id);
        if (topicDO!=null){
            QuestionTopicDO entity = new QuestionTopicDO();
            entity.setQuestionId(topicDO.getQuestionId());
            entity.setCode(topicDO.getCode()+"(复制)");
            entity.setName(topicDO.getName()+"(复制)");
            entity.setType(topicDO.getType());
            entity.setOptContent(topicDO.getOptContent());
            if (topicDO.getOrderNo()!=null){
                entity.setOrderNo(topicDO.getOrderNo()+1);
            }
            questionTopicMapper.insert(entity);
        }
    }

    private void validateQuestionTopicExists(Long id) {
        if (questionTopicMapper.selectById(id) == null) {
            throw exception(QUESTION_TOPIC_NOT_EXISTS);
        }
    }

    @Override
    public QuestionTopicDO getQuestionTopic(Long id) {
        return questionTopicMapper.selectById(id);
    }

    @Override
    public PageResult<QuestionTopicDO> getQuestionTopicPage(QuestionTopicPageReqVO pageReqVO) {
        if (pageReqVO.getQuestionId()==null){
            return PageResult.empty();
        }
        return questionTopicMapper.selectPage(pageReqVO);
    }

    @Override
    public QuestionTopicDO getQuestionTopic(Long questionId, String topicCode, Integer currentNo, String orderBy) {
        if (questionId!=null){
            LambdaQueryWrapper<QuestionTopicDO> wrapper = new LambdaQueryWrapper<>();
            wrapper = wrapper.eq(QuestionTopicDO::getQuestionId,questionId);
            if (StringUtil.isNotEmpty(topicCode)){
                wrapper = wrapper.eq(QuestionTopicDO::getCode,topicCode);
            }else{
                if (StringUtil.isEmpty(orderBy)){
                    orderBy = "next";
                }

                if (orderBy.equalsIgnoreCase("next")){
                    if (currentNo!=null){
                        wrapper = wrapper.gt(QuestionTopicDO::getOrderNo,currentNo);
                    }

                    wrapper = wrapper.orderByAsc(QuestionTopicDO::getOrderNo);
                }else{
                    if (currentNo!=null){
                        wrapper = wrapper.lt(QuestionTopicDO::getOrderNo,currentNo);
                    }
                    wrapper = wrapper.orderByDesc(QuestionTopicDO::getOrderNo);
                }
            }

            PageParam pageParam = new PageParam();
            pageParam.setPageNo(1);
            pageParam.setPageSize(1);
            PageResult<QuestionTopicDO>  page = questionTopicMapper.selectPage(pageParam,wrapper);
            if (page!=null && page.getList().size()>0){
                return page.getList().get(0);
            }
        }
        return null;
    }

    @Override
    public Long getTotalCount(Long questionId) {
        if (questionId!=null){
            LambdaQueryWrapper<QuestionTopicDO> wrapper = new LambdaQueryWrapper<>();
            wrapper = wrapper.eq(QuestionTopicDO::getQuestionId,questionId);
            return questionTopicMapper.selectCount(wrapper);
        }
        return 0L;
    }

    @Override
    public List<QuestionTopicDO> getQuestionTopicList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return questionTopicMapper.selectBatchIds(ids);
    }

    public QuestionTopicDO getQuestionTopicByCode(Long questionId,String code){
        LambdaQueryWrapper<QuestionTopicDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(QuestionTopicDO::getQuestionId,questionId)
                .eq(QuestionTopicDO::getCode,code);
        List<QuestionTopicDO> list = questionTopicMapper.selectList(wrapper);
        if (list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void importExcel(MultipartFile file, Long id, Boolean updateSupport) {
        try {
            List<QuestionTopicImportVO> list = ExcelUtils.read(file, QuestionTopicImportVO.class);
            if (list!=null && list.size()>0){
                for(QuestionTopicImportVO dd:list){
                    if (StringUtil.isNotEmpty(dd.getCode()) && StringUtil.isNotEmpty(dd.getName())){
                        QuestionTopicDO dto = new QuestionTopicDO();
                        dto.setQuestionId(id);
                        dto.setCode(dd.getCode());
                        dto.setName(dd.getName());
                        dto.setOrderNo(dd.getOrderNo());
                        dto.setType(dd.getType());

                        JSONArray array = new JSONArray();
                        array = addQuestionTopicOpt(array,dd.getOptValue1(),dd.getOptContent1(),dd.getOptScore1());
                        array = addQuestionTopicOpt(array,dd.getOptValue2(),dd.getOptContent2(),dd.getOptScore2());
                        array = addQuestionTopicOpt(array,dd.getOptValue3(),dd.getOptContent3(),dd.getOptScore3());
                        array = addQuestionTopicOpt(array,dd.getOptValue4(),dd.getOptContent4(),dd.getOptScore4());
                        array = addQuestionTopicOpt(array,dd.getOptValue5(),dd.getOptContent5(),dd.getOptScore5());
                        array = addQuestionTopicOpt(array,dd.getOptValue6(),dd.getOptContent6(),dd.getOptScore6());
                        array = addQuestionTopicOpt(array,dd.getOptValue7(),dd.getOptContent7(),dd.getOptScore7());
                        array = addQuestionTopicOpt(array,dd.getOptValue8(),dd.getOptContent8(),dd.getOptScore8());

                        dto.setOptContent(array.toJSONString());
                        if (updateSupport!=null && updateSupport){
                            QuestionTopicDO existsData = getQuestionTopicByCode(id,dd.getCode());
                            if (existsData!=null){
                                dto.setId(existsData.getId());
                            }
                        }
                        if (dto.getId()==null){
                            questionTopicMapper.insert(dto);
                        }else{
                            questionTopicMapper.updateById(dto);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<QuestionTopicDO> getQuestionTopicListById(Long questionId) {
        LambdaQueryWrapper<QuestionTopicDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(QuestionTopicDO::getQuestionId,questionId)
                .orderByAsc(QuestionTopicDO::getOrderNo,QuestionTopicDO::getId);
        return questionTopicMapper.selectList(wrapper);
    }

    public JSONArray addQuestionTopicOpt(JSONArray array,String optValue,String optContent,Integer optScore){
        if (StringUtil.isNotEmpty(optValue) && StringUtil.isNotEmpty(optContent) && optScore!=null){
            JSONObject obj = new JSONObject();
            obj.put("value",optValue);
            obj.put("name",optContent);
            obj.put("score",optScore);
            array.add(obj);
            return array;
        }
        return array;
    }

}