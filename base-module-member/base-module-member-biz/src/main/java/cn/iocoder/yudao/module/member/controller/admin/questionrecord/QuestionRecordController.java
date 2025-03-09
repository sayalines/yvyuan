package cn.iocoder.yudao.module.member.controller.admin.questionrecord;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.api.question.QuestionApi;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionDTO;
import cn.iocoder.yudao.module.system.api.questiontopic.QuestionTopicApi;
import cn.iocoder.yudao.module.system.api.questiontopic.dto.QuestionTopicDTO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.*;
import javax.servlet.http.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.admin.questionrecord.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.questionrecord.QuestionRecordDO;
import cn.iocoder.yudao.module.member.service.questionrecord.QuestionRecordService;

@Tag(name = "管理后台 - 问卷记录")
@RestController
@RequestMapping("/member/question-record")
@Validated
public class QuestionRecordController {

    @Resource
    private QuestionRecordService questionRecordService;

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private QuestionApi questionApi;

    @Resource
    private QuestionTopicApi questionTopicApi;



    @PostMapping("/create")
    @Operation(summary = "创建问卷记录")
    @PreAuthorize("@ss.hasPermission('member:question-record:create')")
    public CommonResult<Long> createQuestionRecord(@Valid @RequestBody QuestionRecordSaveReqVO createReqVO) {
        return success(questionRecordService.createQuestionRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新问卷记录")
    @PreAuthorize("@ss.hasPermission('member:question-record:update')")
    public CommonResult<Boolean> updateQuestionRecord(@Valid @RequestBody QuestionRecordSaveReqVO updateReqVO) {
        questionRecordService.updateQuestionRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除问卷记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:question-record:delete')")
    public CommonResult<Boolean> deleteQuestionRecord(@RequestParam("id") Long id) {
        questionRecordService.deleteQuestionRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得问卷记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:question-record:query')")
    public CommonResult<QuestionRecordRespVO> getQuestionRecord(@RequestParam("id") Long id) {
        QuestionRecordDO questionRecord = questionRecordService.getQuestionRecord(id);
        QuestionRecordRespVO respVO = BeanUtils.toBean(questionRecord, QuestionRecordRespVO.class);
        if (respVO!=null){
            if (respVO.getUserId()!=null){
                MemberUserDO memberUserDO = memberUserService.getUser(respVO.getUserId());
                if (memberUserDO!=null){
                    respVO.setNickname(memberUserDO.getNickname());
                    respVO.setMobile(memberUserDO.getMobile());
                }
            }

            if (respVO.getQuestionId()!=null){
                Set<Long> questionIds = new HashSet<>();
                questionIds.add(respVO.getQuestionId());
                List<QuestionDTO> questionList = questionApi.getQuestionList(questionIds);
                if (questionList != null && questionList.size() > 0) {
                    QuestionDTO questionDTO = questionList.get(0);
                    if (questionDTO!=null){
                        respVO.setQuestionName(questionDTO.getName());
                    }
                }
            }
        }
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得问卷记录分页")
    @PreAuthorize("@ss.hasPermission('member:question-record:query')")
    public CommonResult<PageResult<QuestionRecordRespVO>> getQuestionRecordPage(@Valid QuestionRecordPageReqVO pageReqVO) {
        PageResult<QuestionRecordDO> pageResult = questionRecordService.getQuestionRecordPage(pageReqVO);
        PageResult<QuestionRecordRespVO> page = BeanUtils.toBean(pageResult, QuestionRecordRespVO.class);
        if (page!=null && page.getList()!=null && page.getList().size()>0) {
            Set<Long> userIds = CollectionUtils.convertSet(page.getList(), QuestionRecordRespVO::getUserId);
            if (userIds != null && userIds.size() > 0) {
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList != null && userList.size() > 0) {
                    Map<Long,MemberUserDO> userMap = CollectionUtils.convertMap(userList,MemberUserDO::getId);
                    for(QuestionRecordRespVO dd:page.getList()) {
                        if (dd.getUserId() != null && userMap.containsKey(dd.getUserId())) {
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setNickname(userDO.getNickname());
                            dd.setMobile(userDO.getMobile());
                        }
                    }
                }
            }

            Set<Long> questionIds = CollectionUtils.convertSet(page.getList(), QuestionRecordRespVO::getQuestionId);
            if (questionIds != null && questionIds.size() > 0) {
                List<QuestionDTO> questionList = questionApi.getQuestionList(questionIds);
                if (questionList != null && questionList.size() > 0) {
                    Map<Long,QuestionDTO> questionMap = CollectionUtils.convertMap(questionList,QuestionDTO::getId);
                    for(QuestionRecordRespVO dd:page.getList()) {
                        if (dd.getQuestionId() != null && questionMap.containsKey(dd.getQuestionId())) {
                            QuestionDTO questionDTO = questionMap.get(dd.getQuestionId());
                            dd.setQuestionName(questionDTO.getName());
                        }
                    }
                }
            }
        }
        return success(page);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出问卷记录 Excel")
    @PreAuthorize("@ss.hasPermission('member:question-record:export')")
    @OperateLog(type = EXPORT)
    public void exportQuestionRecordExcel(@Valid QuestionRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        Long questionID = pageReqVO.getQuestionId();
        if (questionID==null){
            return;
        }
        QuestionDTO questionDTO = questionApi.getQuestion(questionID);
        if (questionDTO==null){
            return;
        }

        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<QuestionRecordDO> list = questionRecordService.getQuestionRecordPage(pageReqVO).getList();
        List<QuestionRecordRespVO> resList = BeanUtils.toBean(list, QuestionRecordRespVO.class);
        if (resList!=null && resList.size()>0) {
            Set<Long> userIds = CollectionUtils.convertSet(resList, QuestionRecordRespVO::getUserId);
            if (userIds != null && userIds.size() > 0) {
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList != null && userList.size() > 0) {
                    Map<Long,MemberUserDO> userMap = CollectionUtils.convertMap(userList,MemberUserDO::getId);
                    for(QuestionRecordRespVO dd:resList) {
                        if (dd.getUserId() != null && userMap.containsKey(dd.getUserId())) {
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setNickname(userDO.getNickname());
                            dd.setMobile(userDO.getMobile());
                            dd.setAge(userDO.getAge());
                            dd.setSex(formatSex(userDO.getSex()));
                        }
                    }
                }
            }
        }

        //获取所有题目
        List<String> topicIds = new ArrayList<>();
        Map<String,String> topicMap = new HashMap<>();
        List<QuestionTopicDTO> topicList = questionTopicApi.getQuestionTopicListById(questionID);
        if (topicList!=null && topicList.size()>0){
            for(QuestionTopicDTO dd:topicList){
                topicIds.add(dd.getId().toString());
                topicMap.put(dd.getId().toString(),dd.getName());
            }
        }


        //头部
        List<Object> headList = new ArrayList<>();
        headList.add("ID");
        headList.add("用户ID");
        headList.add("用户昵称");
        headList.add("手机号");
        headList.add("年龄");
        headList.add("性别");
        headList.add("问卷ID");
        headList.add("量表主题");
        headList.add("测评时间");
        headList.add("答题开始时间");
        headList.add("答题结束时间");
        headList.add("答题时间");
        for(String key:topicIds){
            headList.add(topicMap.get(key));
        }
        //数据
        List<List<Object>> dataList = new ArrayList<>();
        if (resList!=null && resList.size()>0){
            for(QuestionRecordRespVO dd:resList){
                List<Object> data = new ArrayList<>();
                data.add(dd.getId());
                data.add(dd.getUserId());
                data.add(formatString(dd.getNickname()));
                data.add(formatString(dd.getMobile()));
                data.add(formatString(dd.getAge()));
                data.add(formatString(dd.getSex()));
                data.add(dd.getQuestionId());
                data.add(formatString(questionDTO.getName()));
                data.add(formatLocalDateTime(dd.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
                data.add(formatLocalDateTime(dd.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
                data.add(formatLocalDateTime(dd.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
                data.add(formatString(dd.getAnswerTime()));
                //解析问卷答案
                Map<String,String> answerMap = new HashMap<>();
                if (StringUtils.isNotEmpty(dd.getAnswer())){
                    JSONArray array = JSONArray.parseArray(dd.getAnswer());
                    if (array!=null && array.size()>0){
                        for(int i=0;i<array.size();i++){
                            JSONObject obj = array.getJSONObject(i);
                            String topicId = obj.getString("id");
                            String score = obj.getString("score");
                            if (StringUtils.isNotEmpty(topicId) && StringUtils.isNotEmpty(score)){
                                answerMap.put(topicId,score);
                            }
                        }
                    }
                }
                for(String key:topicIds){
                    String value="";
                    if (answerMap.containsKey(key)){
                        value = answerMap.get(key);
                    }
                    data.add(value);
                }
                dataList.add(data);
            }
        }
        //结果集
        List<List<Object>> result = new ArrayList<>();
        result.add(headList);
        result.addAll(dataList);
        // 导出 Excel
        cn.iocoder.yudao.module.member.utils.ExcelUtils.export(response,"问卷记录.xls","数据",result);
    }

    /**
     * 格式化性别
     * @param sex
     * @return
     */
    public String formatSex(Integer sex){
        String result = "未知";
        if(sex!=null){
            if (sex.compareTo(1)==0){
                result = "男";
            }else if (sex.compareTo(2)==0) {
                result = "女";
            }
        }
        return result;
    }

    /**
     * 格式化整数
     * @param data
     * @return
     */
    public String formatString(String data){
        String result = "";
        if (StringUtils.isNotEmpty(data)){
            result =data;
        }
        return result;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @return
     */
    public String formatLocalDateTime(LocalDateTime dateTime, String formatter){
        String result = "";
        if (dateTime!=null){
            if (StringUtils.isEmpty(formatter)){
                formatter = "yyyy-MM-dd HH:mm:ss";
            }
            result = dateTime.format(DateTimeFormatter.ofPattern(formatter));
        }
        return result;
    }

}