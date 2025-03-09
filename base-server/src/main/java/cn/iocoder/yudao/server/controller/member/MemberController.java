package cn.iocoder.yudao.server.controller.member;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.module.member.api.questionrecord.QuestionRecordApi;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordDTO;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordPageDTO;
import cn.iocoder.yudao.module.member.api.questionrecord.dto.QuestionRecordRespDTO;
import cn.iocoder.yudao.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.level.MemberLevelService;
import cn.iocoder.yudao.module.member.service.messageremind.MessageRemindService;
import cn.iocoder.yudao.module.member.service.point.MemberPointRecordService;
import cn.iocoder.yudao.module.member.service.signin.MemberSignInConfigService;
import cn.iocoder.yudao.module.member.service.signin.MemberSignInRecordService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.promotion.service.article.ArticleService;
import cn.iocoder.yudao.module.promotion.service.coupon.CouponService;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.question.QuestionApi;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionDTO;
import cn.iocoder.yudao.module.system.api.questiontopic.QuestionTopicApi;
import cn.iocoder.yudao.module.system.api.questiontopic.dto.QuestionTopicDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.questiondimension.QuestionDimensionDO;
import cn.iocoder.yudao.module.system.dal.dataobject.questionfactor.QuestionFactorDO;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import cn.iocoder.yudao.module.system.service.questiondimension.QuestionDimensionService;
import cn.iocoder.yudao.module.system.service.questionfactor.QuestionFactorService;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderQueryService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.util.HttpClientUtils;
import cn.iocoder.yudao.server.util.ResponseResult;
import cn.iocoder.yudao.server.vo.ApiAnswerDO;
import cn.iocoder.yudao.server.vo.ApiKeywordContentDO;
import cn.iocoder.yudao.server.vo.ApiQuestionFactorOrDimensionDO;
import cn.iocoder.yudao.server.vo.ApiScoreDO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 会员对外接口
 */
@Tag(name = "对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/member")
public class MemberController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Resource
    private MemberUserService memberUserService;
    @Resource
    private MemberPointRecordService pointRecordService;
    @Resource
    private MemberSignInRecordService signInRecordService;
    @Resource
    private MemberSignInConfigService signInConfigService;
    @Resource
    private CouponService couponService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;
    @Resource
    private MessageRemindService messageRemindService;
    @Resource
    private ArticleService articleService;
    @Resource
    private MemberLevelService memberLevelService;
    @Resource
    private QuestionTopicApi questionTopicApi;
    @Resource
    private QuestionRecordApi questionRecordApi;
    @Resource
    private DictDataService dictDataService;
    @Resource
    private DeptApi deptApi;
    @Resource
    private QuestionApi questionApi;
    @Resource
    private QuestionFactorService questionFactorService;
    @Resource
    private QuestionDimensionService questionDimensionService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 查看用户信息
     *
     * @return
     */
    @RequestMapping(value = "/info")
    @ResponseBody
    public ResponseResult memberInfo(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        String levelName = "";
        if (member.getLevelId() != null) {
            MemberLevelDO memberLevelDO = memberLevelService.getLevel(member.getLevelId());
            if (memberLevelDO != null) {
                levelName = memberLevelDO.getName();
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", member.getId());
        data.put("nickname", member.getNickname());
        data.put("name", member.getName());
        data.put("avatar", member.getAvatar());
        data.put("mobile", member.getMobile());
        data.put("levelId", member.getLevelId());
        data.put("levelName", levelName);
        data.put("sex", CommonUtils.formatSex(member.getSex()));
        data.put("birthday", CommonUtils.formatLocalDateTime(member.getBirthday(), "yyyy-MM-dd"));
        data.put("point", member.getPoint());
        data.put("idCode", member.getIdCode());
        data.put("idCodeType", member.getIdCodeType());
        data.put("userType", member.getUserType());
        data.put("age",member.getAge());
        data.put("deptId", member.getDeptId());
        data.put("deptName", "");
        data.put("areaId",member.getAreaId());
        String areaName ="";
        if (member.getAreaId()!=null){
            areaName = AreaUtils.format(member.getAreaId());
        }
        data.put("areaName",areaName);
        if (member.getDeptId()!=null){
            DeptRespDTO deptRespDTO = deptApi.getDept(member.getDeptId());
            if (deptRespDTO!=null){
                data.put("deptName", deptRespDTO.getName());
            }
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(data);
        return rr;
    }

    /**
     * 修改用户信息
     *
     * @return
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public ResponseResult updateMemberInfo(String nickname, String logo, String sex, String age, String mobile, Long deptId,Integer areaId,
                                           String name, Integer userType,String idCode,Integer idCodeType, HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        try {
            if (nickname!=null){
                member.setNickname(nickname);
            }
            if (logo!=null){
                member.setAvatar(logo);
            }
            if (sex!=null){
                member.setSex(CommonUtils.toFormatSex(sex));
            }
            member.setIsRegister(true);
            if (deptId!=null){
                member.setDeptId(deptId);
            }
            if (name!=null){
                member.setName(name);
            }
            if (userType!=null){
                member.setUserType(userType);
                //如果是社会成员，清除工会信息
                if (userType.equals(2)){
                    member.setDeptId(null);
                }
            }

            if (mobile!=null){
                member.setMobile(mobile);
            }
            if (idCode!=null){
                member.setIdCode(idCode);
            }
            if (idCodeType!=null){
                member.setIdCodeType(idCodeType);
            }
            if (age!=null){
                member.setAge(age);
            }
            if (areaId!=null){
                member.setAreaId(areaId);
            }
            memberUserService.perfectUserInfo(member);
            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            return rr;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        rr.setCode(ResponseResult.ERROR);
        rr.setMessage("操作失败");
        return rr;
    }

    /**
     * 账号+密码登录
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseResult login(HttpServletRequest request,String username,String pwd) {
        ResponseResult rr = new ResponseResult();
        List<Map<String,String>> errorList = new ArrayList<>();
        if (StringUtils.isEmpty(username)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("请输入账号");
            return  rr;
        }
        if (StringUtils.isEmpty(pwd)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("请输入密码");
            return  rr;
        }

        MemberUserDO member = memberUserService.getUserByMobile(username);
        if (member==null) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("用户名或密码错误");
            return  rr;
        }else{
            if (member.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("账号已停用");
                return  rr;
            }
        }

        if (!memberUserService.isPasswordMatch(pwd, member.getPassword())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("用户名或密码错误");
            return  rr;
        }

        String levelName = "";
        if (member.getLevelId() != null) {
            MemberLevelDO memberLevelDO = memberLevelService.getLevel(member.getLevelId());
            if (memberLevelDO != null) {
                levelName = memberLevelDO.getName();
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", member.getId());
        data.put("token",createToken(getSocialUserId(member.getId())));
        data.put("nickname", member.getNickname());
        data.put("name", member.getName());
        data.put("avatar", member.getAvatar());
        data.put("mobile", member.getMobile());
        data.put("levelId", member.getLevelId());
        data.put("levelName", levelName);
        data.put("sex", CommonUtils.formatSex(member.getSex()));
        data.put("birthday", CommonUtils.formatLocalDateTime(member.getBirthday(), "yyyy-MM-dd"));
        data.put("point", member.getPoint());
        data.put("idCode", member.getIdCode());
        data.put("idCodeType", member.getIdCodeType());
        data.put("userType", member.getUserType());
        data.put("age",member.getAge());
        data.put("deptId", member.getDeptId());
        data.put("deptName", "");
        data.put("areaId",member.getAreaId());
        String areaName ="";
        if (member.getAreaId()!=null){
            areaName = AreaUtils.format(member.getAreaId());
        }
        data.put("areaName",areaName);
        if (member.getDeptId()!=null){
            DeptRespDTO deptRespDTO = deptApi.getDept(member.getDeptId());
            if (deptRespDTO!=null){
                data.put("deptName", deptRespDTO.getName());
            }
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(data);
        return rr;
    }

    /**
     * 修改密码
     * @return
     */
    @PostMapping(value = "/modify_pwd")
    @ResponseBody
    public ResponseResult modifyPwd(HttpServletRequest request,String newPwd) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        try{
            if (StringUtils.isEmpty(newPwd)){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("请输入新密码");
                return  rr;
            }
//            member = memberUserService.getUserByMobile("weifang");
//            newPwd="weifang2024";

            member.setPassword(passwordEncoder.encode(newPwd));
            memberUserService.updateUser(member);

            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            return rr;
        }catch (Exception e){
            logger.error(e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            if (e.getMessage().length()>100){
                rr.setMessage("操作失败");
            }else{
                rr.setMessage(e.getMessage());
            }
            return rr;
        }
    }

    /**
     * 修改用户手机号/邮箱/身份证信息
     * @return
     */
    @PostMapping(value = "/update_to")
    @ResponseBody
    public ResponseResult updateMemberMobile(String phone, Integer type, HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        try {
            switch (type) {
                // 1修改手机号
                case 1:
                    member.setMobile(phone);
                    break;
                // 1修改邮箱
                case 2:
                    member.setEmail(phone);
                    break;
                // 1修改身份证
                case 3:
                    member.setIdCode(phone);
                    break;
                default:
                    return new ResponseResult(ResponseResult.ERROR, "操作失败", null);
            }

            member.setIsRegister(true);
            memberUserService.updateUser(member);
            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            return rr;
        } catch (Exception e) {
            logger.error(e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            if (e.getMessage().length() > 100) {
                rr.setMessage("操作失败");
            } else {
                rr.setMessage(e.getMessage());
            }
            return rr;
        }
    }

    /**
     * 修改用户身份证类型信息
     *
     * @return
     */
    @PostMapping(value = "/update_code_type")
    @ResponseBody
    public ResponseResult updateMemberMobile(Integer type, HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        try {

            member.setIdCodeType(type);

            member.setIsRegister(true);
            memberUserService.updateUser(member);
            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            return rr;
        } catch (Exception e) {
            logger.error(e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            if (e.getMessage().length() > 100) {
                rr.setMessage("操作失败");
            } else {
                rr.setMessage(e.getMessage());
            }
            return rr;
        }
    }

    /**
     * 提交问卷
     *
     * @return
     */
    @PostMapping(value = "/submit_question")
    @ResponseBody
    public ResponseResult submitQuestion(@RequestBody String param, HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
//        if (member == null) {
//            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
//        }

        try {
            if (StringUtils.isEmpty(param)) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("缺少参数");
                return rr;
            }

            JSONObject obj = JSON.parseObject(param);
            if (!obj.containsKey("questionId") || StringUtils.isEmpty(obj.getString("questionId"))) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("缺少参数questionId");
                return rr;
            }
            Long questionId = obj.getLong("questionId");

            if (!obj.containsKey("answer") || StringUtils.isEmpty(obj.getString("answer"))) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("缺少参数answer");
                return rr;
            }

            if (!obj.containsKey("startTime") || StringUtils.isEmpty(obj.getString("startTime"))) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("缺少参数startTime");
                return rr;
            }

            if (!obj.containsKey("endTime") || StringUtils.isEmpty(obj.getString("endTime"))) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("缺少参数endTime");
                return rr;
            }

            QuestionDTO questionDO = questionApi.getQuestion(questionId);
            if (questionDO==null){
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("量表记录不存在");
                return rr;
            }

            LocalDateTime startTime = CommonUtils.toFormatLocalDateTime(obj.getString("startTime"),"yyyy-MM-dd HH:mm:ss");
            LocalDateTime endTime = CommonUtils.toFormatLocalDateTime(obj.getString("endTime"),"yyyy-MM-dd HH:mm:ss");
            Duration duration = Duration.between(startTime, endTime);
            //获取总间隔时间（秒）
            long seconds = duration.getSeconds();
            //将间隔时间转换为 时 - 分 - 秒
            //计算间隔时间（时）
            int h = seconds > 3600 ? (int) (seconds / 3600) : 0;
            //计算间隔时间（分）
            int m = (seconds - 3600 * h) > 60 ? (int) ((seconds - 3600 * h) / 60) : 0;
            //计算间隔时间（秒）
            int s = (int) (seconds - 3600 * h - 60 * m);
            String answerTime="";
            if (h>0){
                answerTime = answerTime+h+"小时";
            }
            if (h>0 || m>0){
                answerTime = answerTime+m+"分";
            }
            if (h>0 || m>0 || s>0){
                answerTime = answerTime+s+"秒";
            }
            Set<Long> topicIds = new HashSet<>();
            JSONArray answerArray = obj.getJSONArray("answer");
            for (int i = 0; i < answerArray.size(); i++) {
                JSONObject subObj = answerArray.getJSONObject(i);
                String topicId = CommonUtils.formatString(subObj.getString("id"));
                if (StringUtils.isNotEmpty(topicId)) {
                    topicIds.add(Long.valueOf(topicId));
                }
            }

            List<ApiAnswerDO> answerList = new ArrayList<>();
            Map<Long, ApiAnswerDO> answerMap = new HashMap<>();

            Map<Long, QuestionTopicDTO> questionTopicMap = new HashMap<>();
            if (topicIds.size() > 0) {
                List<QuestionTopicDTO> questionTopicList = questionTopicApi.getQuestionTopicList(topicIds);
                if (questionTopicList != null) {
                    for (QuestionTopicDTO dd : questionTopicList) {
                        questionTopicMap.put(dd.getId(), dd);
                    }
                }

                for (int i = 0; i < answerArray.size(); i++) {
                    JSONObject subObj = answerArray.getJSONObject(i);
                    Long topicId = CommonUtils.formatLongValue(subObj.getLong("id"));
                    String topicValue = CommonUtils.formatString(subObj.getString("value"));
                    if (topicId!=null && questionTopicMap.containsKey(topicId)) {
                        QuestionTopicDTO questionTopic = questionTopicMap.get(topicId);

                        //封装其他数据
                        String answerDesc = "";
                        Integer score = 0;
                        if (StringUtils.isNotEmpty(topicValue)){
                            for(String ss:topicValue.split(",")){
                                if (StringUtils.isNotEmpty(ss)){
                                    JSONObject valueObj = getJSONObjectByValue(questionTopic.getOptContent(),"value",ss);
                                    if (valueObj!=null){
                                        String tmpContent = ss+"、"+CommonUtils.formatString(valueObj.getString("name"));
                                        if (StringUtils.isNotEmpty(answerDesc)){
                                            answerDesc = answerDesc+";"+tmpContent;
                                        }else{
                                            answerDesc = tmpContent;
                                        }
                                        score = score+CommonUtils.formatInteger(valueObj.getInteger("score"));
                                    }
                                }
                            }
                        }

                        ApiAnswerDO answerDO = new ApiAnswerDO();
                        answerDO.setId(topicId);
                        answerDO.setIndexNo(i + 1);
                        answerDO.setName(CommonUtils.formatString(questionTopic.getName()));
                        answerDO.setAnswer(topicValue);
                        answerDO.setAnswerDesc(answerDesc);
                        answerDO.setScore(score);
                        answerList.add(answerDO);

                        answerMap.put(answerDO.getId(),answerDO);
                    }
                }
            }

            //计算分值
            String resultTitle = "";
            String resultContent = "";
            String resultParam = "";
            String resultStatus = "";
            //因子结果列表
            List<ApiQuestionFactorOrDimensionDO> factorResultList = new ArrayList<>();
            //维度结果列表
            List<ApiQuestionFactorOrDimensionDO> dimensionResultList = new ArrayList<>();
            //总分结果列表
            List<ApiQuestionFactorOrDimensionDO> totalScoreResultList = new ArrayList<>();

            //因子列表
            List<QuestionFactorDO> factorList = questionFactorService.findList(questionId);
            Map<Long, ApiQuestionFactorOrDimensionDO> factorMap = new HashMap<>();
            if (factorList!=null && factorList.size()>0){
                for(QuestionFactorDO dd:factorList){
                    String optContent = dd.getOptContent();
                    List<Long> topics = dd.getTopics();
                    ApiQuestionFactorOrDimensionDO dto = BeanUtils.toBean(dd, ApiQuestionFactorOrDimensionDO.class);
                    dto.setKeywordList(getKeywordList(dd.getKeywordContent()));
                    Integer scoreType = dto.getScoreType();
                    BigDecimal constantValue = dto.getConstantValue();
                    //默认求和
                    if (scoreType==null){
                        scoreType=0;
                    }

                    if (constantValue==null){
                        constantValue = BigDecimal.ZERO;
                    }

                    BigDecimal score = BigDecimal.ZERO;
                    //获取选中题目分值
                    if (topics!=null && topics.size()>0){
                        for(int j=0;j<topics.size();j++){
                            Long topicId = Long.valueOf(String.valueOf(topics.get(j)));
                            if (answerMap.containsKey(topicId)){
                                ApiAnswerDO answerDO = answerMap.get(topicId);
                                if (answerDO!=null && answerDO.getScore()!=null){
                                    score = score.add(new BigDecimal(answerDO.getScore()));
                                }
                            }
                        }

                        if (scoreType.equals(1)){
                            //求平均值
                            score = score.divide(new BigDecimal(topics.size()),0, RoundingMode.HALF_DOWN);
                        }else if (scoreType.equals(2)){
                            //求和乘以常量
                            score = score.multiply(constantValue).setScale(0, BigDecimal.ROUND_HALF_DOWN);
                        } else if (scoreType.equals(3)){
                            //求和除以常量
                            if (constantValue.compareTo(BigDecimal.ZERO)!=0){
                                score = score.divide(constantValue,0, RoundingMode.HALF_DOWN);
                            }else{
                                score = BigDecimal.ZERO;
                            }
                        } else if (scoreType.equals(4)){
                            //求平均值乘以常量
                            score = score.divide(new BigDecimal(topics.size()),2, RoundingMode.HALF_DOWN);
                            score = score.multiply(constantValue).setScale(0, BigDecimal.ROUND_HALF_DOWN);
                        } else if (scoreType.equals(5)){
                            //求平均值除以常量
                            if (constantValue.compareTo(BigDecimal.ZERO)!=0){
                                score = score.divide(new BigDecimal(topics.size()),2, RoundingMode.HALF_DOWN);
                                score = score.divide(constantValue,0, RoundingMode.HALF_DOWN);
                            }else{
                                score = BigDecimal.ZERO;
                            }
                        }
                    }
                    //根据分值匹配配置
                    ApiScoreDO scoreDetail = null;
                    if (StringUtils.isNotEmpty(optContent)){
                        JSONArray tmpArray = JSONArray.parseArray(optContent);
                        if (tmpArray!=null && tmpArray.size()>0){
                            for(int i=0;i<tmpArray.size();i++){
                                JSONObject tmpObj = tmpArray.getJSONObject(i);
                                String t_name=CommonUtils.formatString(tmpObj.getString("name"));
                                BigDecimal t_scoreBegin=CommonUtils.formatBigDecimal(tmpObj.getString("scoreBegin"));
                                Integer t_scoreBeginType=CommonUtils.formatInteger(tmpObj.getInteger("scoreBeginType"));
                                BigDecimal t_scoreEnd=CommonUtils.formatBigDecimal(tmpObj.getString("scoreEnd"));
                                Integer t_scoreEndType=CommonUtils.formatInteger(tmpObj.getInteger("scoreEndType"));
                                String t_description=CommonUtils.formatString(tmpObj.getString("description"));
                                String t_suggest=CommonUtils.formatString(tmpObj.getString("suggest"));

                                Boolean isOk = false;
                                if (t_scoreBeginType.equals(0)){
                                    //大于分值
                                    if (score.compareTo(t_scoreBegin)>0){
                                        isOk = true;
                                    }
                                }else if (t_scoreBeginType.equals(1)){
                                    //大于等于分值
                                    if (score.compareTo(t_scoreBegin)>=0){
                                        isOk = true;
                                    }
                                }

                                if (isOk){
                                    isOk = false;
                                    if (t_scoreEndType.equals(0)){
                                        //小于分值
                                        if (score.compareTo(t_scoreEnd)<0){
                                            isOk = true;
                                        }
                                    }else if (t_scoreEndType.equals(1)){
                                        //小于等于分值
                                        if (score.compareTo(t_scoreEnd)<=0){
                                            isOk = true;
                                        }
                                    }
                                }

                                if (isOk){
                                    scoreDetail = new ApiScoreDO();
                                    scoreDetail.setName(t_name);
                                    scoreDetail.setScoreBegin(t_scoreBegin);
                                    scoreDetail.setScoreBeginType(t_scoreBeginType);
                                    scoreDetail.setScoreEnd(t_scoreEnd);
                                    scoreDetail.setScoreEndType(t_scoreEndType);
                                    scoreDetail.setDescription(t_description);
                                    scoreDetail.setSuggest(t_suggest);
                                    break;
                                }


                            }
                        }
                    }

                    dto.setScore(score);
                    dto.setScoreInt(score.intValue());
                    dto.setScoreDetail(scoreDetail);
                    if (scoreDetail!=null && dto.getIsShow()!=null && dto.getIsShow().equals(1)){
                        if (dto.getIsTotal()!=null && dto.getIsTotal().equals(1)){
                            totalScoreResultList.add(dto);
                        }else{
                            factorResultList.add(dto);
                        }
                    }
                    factorMap.put(dto.getId(),dto);
                }
            }

            //维度列表
            List<QuestionDimensionDO> dimensionList = questionDimensionService.findList(questionId);
            if (dimensionList!=null && dimensionList.size()>0){
                for(QuestionDimensionDO dd:dimensionList){
                    String optContent = dd.getOptContent();
                    List<Long> factors = dd.getFactors();

                    ApiQuestionFactorOrDimensionDO dto = BeanUtils.toBean(dd,ApiQuestionFactorOrDimensionDO.class);
                    dto.setKeywordList(getKeywordList(dd.getKeywordContent()));
                    Integer scoreType = dto.getScoreType();
                    BigDecimal constantValue = dto.getConstantValue();
                    //默认求和
                    if (scoreType==null){
                        scoreType=0;
                    }

                    if (constantValue==null){
                        constantValue = BigDecimal.ZERO;
                    }

                    BigDecimal score = BigDecimal.ZERO;
                    //获取选中因子分值
                    if (factors!=null && factors.size()>0){
                        for(int j=0;j<factors.size();j++) {
                            Long factorId = Long.valueOf(String.valueOf(factors.get(j)));
                            if (factorMap.containsKey(factorId)){
                                ApiQuestionFactorOrDimensionDO factorDO = factorMap.get(factorId);
                                if (factorDO!=null && factorDO.getScore()!=null){
                                    score = score.add(factorDO.getScore());
                                }
                            }
                        }

                        if (scoreType.equals(1)){
                            //求平均值
                            score = score.divide(new BigDecimal(factors.size()),0, RoundingMode.HALF_DOWN);
                        }else if (scoreType.equals(2)){
                            //求和乘以常量
                            score = score.multiply(constantValue).setScale(0, BigDecimal.ROUND_HALF_DOWN);
                        } else if (scoreType.equals(3)){
                            //求和除以常量
                            if (constantValue.compareTo(BigDecimal.ZERO)!=0){
                                score = score.divide(constantValue,0, RoundingMode.HALF_DOWN);
                            }else{
                                score = BigDecimal.ZERO;
                            }
                        } else if (scoreType.equals(4)){
                            //求平均值乘以常量
                            score = score.divide(new BigDecimal(factors.size()),2, RoundingMode.HALF_DOWN);
                            score = score.multiply(constantValue).setScale(0,BigDecimal.ROUND_HALF_DOWN);
                        } else if (scoreType.equals(5)){
                            //求平均值除以常量
                            if (constantValue.compareTo(BigDecimal.ZERO)!=0){
                                score = score.divide(new BigDecimal(factors.size()),2, RoundingMode.HALF_DOWN);
                                score = score.divide(constantValue,0, RoundingMode.HALF_DOWN);
                            }else{
                                score = BigDecimal.ZERO;
                            }
                        }
                    }
                    //根据分值匹配配置
                    ApiScoreDO scoreDetail = null;
                    if (StringUtils.isNotEmpty(optContent)){
                        JSONArray tmpArray = JSONArray.parseArray(optContent);
                        if (tmpArray!=null && tmpArray.size()>0){
                            for(int i=0;i<tmpArray.size();i++){
                                JSONObject tmpObj = tmpArray.getJSONObject(i);
                                String t_name=CommonUtils.formatString(tmpObj.getString("name"));
                                BigDecimal t_scoreBegin=CommonUtils.formatBigDecimal(tmpObj.getString("scoreBegin"));
                                Integer t_scoreBeginType=CommonUtils.formatInteger(tmpObj.getInteger("scoreBeginType"));
                                BigDecimal t_scoreEnd=CommonUtils.formatBigDecimal(tmpObj.getString("scoreEnd"));
                                Integer t_scoreEndType=CommonUtils.formatInteger(tmpObj.getInteger("scoreEndType"));
                                String t_description=CommonUtils.formatString(tmpObj.getString("description"));
                                String t_suggest=CommonUtils.formatString(tmpObj.getString("comment"));

                                Boolean isOk = false;
                                if (t_scoreBeginType.equals(0)){
                                    //大于分值
                                    if (score.compareTo(t_scoreBegin)>0){
                                        isOk = true;
                                    }
                                }else if (t_scoreBeginType.equals(1)){
                                    //大于等于分值
                                    if (score.compareTo(t_scoreBegin)>=0){
                                        isOk = true;
                                    }
                                }

                                if (isOk){
                                    isOk = false;
                                    if (t_scoreEndType.equals(0)){
                                        //小于分值
                                        if (score.compareTo(t_scoreEnd)<0){
                                            isOk = true;
                                        }
                                    }else if (t_scoreEndType.equals(1)){
                                        //小于等于分值
                                        if (score.compareTo(t_scoreEnd)<=0){
                                            isOk = true;
                                        }
                                    }
                                }

                                if (isOk){
                                    scoreDetail = new ApiScoreDO();
                                    scoreDetail.setName(t_name);
                                    scoreDetail.setScoreBegin(t_scoreBegin);
                                    scoreDetail.setScoreBeginType(t_scoreBeginType);
                                    scoreDetail.setScoreEnd(t_scoreEnd);
                                    scoreDetail.setScoreEndType(t_scoreEndType);
                                    scoreDetail.setDescription(t_description);
                                    scoreDetail.setSuggest(t_suggest);
                                    break;
                                }


                            }
                        }
                    }

                    dto.setScore(score);
                    dto.setScoreInt(score.intValue());
                    dto.setScoreDetail(scoreDetail);
                    if (scoreDetail!=null && dto.getIsShow()!=null && dto.getIsShow().equals(1)){
                        if (dto.getIsTotal()!=null && dto.getIsTotal().equals(1)){
                            totalScoreResultList.add(dto);
                        }else{
                            dimensionResultList.add(dto);
                        }
                    }
                }
            }

            //首要时间观念结果列表
            List<ApiQuestionFactorOrDimensionDO> firstTimeResultList = new ArrayList<>();
            //其他时间观念结果列表
            List<ApiQuestionFactorOrDimensionDO> otherTimeResultList = new ArrayList<>();
            //是否时间观量表
            Boolean isTimeQuestion = false;
            if (questionDO.getId().equals(30L)){
                isTimeQuestion = true;
            }
            if (isTimeQuestion){
                /**
                 * （得分>3分，取得分最高的2个维度名称和报告，并列则按顺序取前2；若只有1个维度>3分，只取一个维度名称和报告。
                 * 若全都<=3分，取分数最高的1个维度。如果所有维度分数相同，取关注过去的积极时间观和关注当下的享乐主义时间观）
                 *
                 */
                if (factorResultList!=null){
                    List<ApiQuestionFactorOrDimensionDO> newList = new ArrayList<>();
                    newList.addAll(factorResultList);
                    //按分值倒叙+排序升序
                    List<ApiQuestionFactorOrDimensionDO> sameDefaultList = new ArrayList<>();
                    for(ApiQuestionFactorOrDimensionDO dd:newList){
                        String customOrder = String.format("%03d",dd.getScoreInt())+String.format("%03d",999-dd.getOrderNo());
                        dd.setCustomOrder(customOrder);
                        if (dd.getId().equals(67L) || dd.getId().equals(68L)){
                            sameDefaultList.add(dd);
                        }
                    }
                    Collections.sort(newList, new Comparator<ApiQuestionFactorOrDimensionDO>() {
                        @Override
                        public int compare(ApiQuestionFactorOrDimensionDO o1, ApiQuestionFactorOrDimensionDO o2) {
                            return o2.getCustomOrder().compareTo(o1.getCustomOrder()); // 字符串倒序
                        }
                    });

                    List<Long> timeIds = new ArrayList<>();
                    for(ApiQuestionFactorOrDimensionDO dd:newList){
                        if ((dd.getScoreInt().compareTo(3)>0) && (firstTimeResultList.size()<2)){
                            timeIds.add(dd.getId());
                            firstTimeResultList.add(dd);
                        }
                    }

                    if(timeIds.isEmpty()){
                        //判度分值是否相同
                        Boolean isSameScore = true;
                        Integer tmpValue = newList.get(0).getScoreInt();
                        for(ApiQuestionFactorOrDimensionDO dd:newList){
                            if (dd.getScoreInt().compareTo(tmpValue)!=0){
                                isSameScore =false;
                                break;
                            }
                        }

                        if (isSameScore){
                            //默认取关注过去的积极时间观和关注当下的享乐主义时间观
                            for(ApiQuestionFactorOrDimensionDO dd:sameDefaultList){
                                timeIds.add(dd.getId());
                                firstTimeResultList.add(dd);
                            }
                        }else{
                            timeIds.add(newList.get(0).getId());
                            firstTimeResultList.add(newList.get(0));
                        }
                    }

                    for(ApiQuestionFactorOrDimensionDO dd:factorResultList){
                        if (timeIds.indexOf(dd.getId())==-1){
                            otherTimeResultList.add(dd);
                        }
                    }
                }
            }


            //封装报告结果参数
            JSONObject resultMap = new JSONObject();
            resultMap.put("isTimeQuestion",isTimeQuestion);
            resultMap.put("question",questionDO);
            if (isTimeQuestion){
                resultMap.put("factorList",factorResultList);
                resultMap.put("firstTimeResultList",firstTimeResultList);
                resultMap.put("otherTimeResultList",otherTimeResultList);
            }else{
                resultMap.put("factorList",factorResultList);
                resultMap.put("dimensionList",dimensionResultList);
                resultMap.put("totalScoreList",totalScoreResultList);
            }
            resultParam = JSONObject.toJSONString(resultMap, SerializerFeature.DisableCircularReferenceDetect);
            //封装报告结果内容
            Integer indexNo = 1;
            resultContent = Integer.valueOf(indexNo)+"、报告说明"+"\n"+questionDO.getDescription();
            if (isTimeQuestion){
                if (firstTimeResultList!=null && firstTimeResultList.size()>0){
                    indexNo=indexNo+1;
                    resultContent = resultContent+"\n"+Integer.valueOf(indexNo)+"、首要时间观念解释与建议";

                    for(ApiQuestionFactorOrDimensionDO dd:firstTimeResultList){
                        String tmpContent = dd.getName();
                        if (dd.getScoreDetail()!=null){
                            String scoreBegin = dd.getScoreDetail().getScoreBegin().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            String scoreEnd = dd.getScoreDetail().getScoreEnd().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            tmpContent = tmpContent+"\n"+"总分【"+scoreBegin+","+scoreEnd+"】 -- "+dd.getScoreDetail().getName();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getDescription();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getSuggest();
                        }
                        resultContent = resultContent+"\n"+tmpContent;
                    }
                }
                if (otherTimeResultList!=null && otherTimeResultList.size()>0){
                    indexNo=indexNo+1;
                    resultContent = resultContent+"\n"+Integer.valueOf(indexNo)+"、其他时间观念解释与建议";

                    for(ApiQuestionFactorOrDimensionDO dd:otherTimeResultList){
                        String tmpContent = dd.getName();
                        if (dd.getScoreDetail()!=null){
                            String scoreBegin = dd.getScoreDetail().getScoreBegin().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            String scoreEnd = dd.getScoreDetail().getScoreEnd().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            tmpContent = tmpContent+"\n"+"总分【"+scoreBegin+","+scoreEnd+"】 -- "+dd.getScoreDetail().getName();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getDescription();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getSuggest();
                        }
                        resultContent = resultContent+"\n"+tmpContent;
                    }
                }
            }else{
                if (totalScoreResultList!=null && totalScoreResultList.size()>0){
                    indexNo=indexNo+1;
                    resultContent = resultContent+"\n"+Integer.valueOf(indexNo)+"、总分解释与建议";
                    //是否统计
                    if (questionDO.getIsStat()!=null && questionDO.getIsStat().equals(1)){
                        ApiQuestionFactorOrDimensionDO firstData = totalScoreResultList.get(0);
                        if (firstData.getScoreDetail()!=null){
                            resultStatus = firstData.getScoreDetail().getName();
                        }
                    }

                    for(ApiQuestionFactorOrDimensionDO dd:totalScoreResultList){
                        String tmpContent = dd.getName();
                        if (dd.getScoreDetail()!=null){
                            String scoreBegin = dd.getScoreDetail().getScoreBegin().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            String scoreEnd = dd.getScoreDetail().getScoreEnd().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            tmpContent = tmpContent+"\n"+"总分【"+scoreBegin+","+scoreEnd+"】 -- "+dd.getScoreDetail().getName();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getDescription();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getSuggest();
                        }
                        resultContent = resultContent+"\n"+tmpContent;
                    }
                }

                if (dimensionResultList!=null && dimensionResultList.size()>0){
                    indexNo=indexNo+1;
                    resultContent = resultContent+"\n"+Integer.valueOf(indexNo)+"、维度解释与建议";

                    for(ApiQuestionFactorOrDimensionDO dd:dimensionResultList){
                        String tmpContent = dd.getName();
                        if (dd.getScoreDetail()!=null){
                            String scoreBegin = dd.getScoreDetail().getScoreBegin().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            String scoreEnd = dd.getScoreDetail().getScoreEnd().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            tmpContent = tmpContent+"\n"+"总分【"+scoreBegin+","+scoreEnd+"】 -- "+dd.getScoreDetail().getName();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getDescription();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getSuggest();
                        }
                        resultContent = resultContent+"\n"+tmpContent;
                    }
                }

                if (factorResultList!=null && factorResultList.size()>0){
                    indexNo=indexNo+1;
                    resultContent = resultContent+"\n"+Integer.valueOf(indexNo)+"、因子解释与建议";

                    for(ApiQuestionFactorOrDimensionDO dd:factorResultList){
                        String tmpContent = dd.getName();
                        if (dd.getScoreDetail()!=null){
                            String scoreBegin = dd.getScoreDetail().getScoreBegin().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            String scoreEnd = dd.getScoreDetail().getScoreEnd().setScale(0,BigDecimal.ROUND_HALF_DOWN).toPlainString();
                            tmpContent = tmpContent+"\n"+"总分【"+scoreBegin+","+scoreEnd+"】 -- "+dd.getScoreDetail().getName();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getDescription();
                            tmpContent = tmpContent+"\n"+dd.getScoreDetail().getSuggest();
                        }
                        resultContent = resultContent+"\n"+tmpContent;
                    }
                }
            }


            QuestionRecordDTO recordDTO = new QuestionRecordDTO();
            if (member != null) {
                recordDTO.setUserId(member.getId());
            }
            recordDTO.setQuestionId(questionId);
            recordDTO.setAnswer(JSONArray.toJSONString(answerList));
            recordDTO.setStartTime(startTime);
            recordDTO.setEndTime(endTime);
            recordDTO.setAnswerTime(answerTime);
            recordDTO.setResultTitle(resultTitle);
            recordDTO.setResult(resultContent);
            recordDTO.setResultParam(resultParam);
            recordDTO.setResultStatus(resultStatus);
            Long dataId=questionRecordApi.saveQuestionRecord(recordDTO);

            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            rr.setData(dataId);
            return rr;
        } catch (Exception e) {
            logger.error(e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            if (e.getMessage().length() > 100) {
                rr.setMessage("操作失败");
            } else {
                rr.setMessage(e.getMessage());
            }
            return rr;
        }
    }

    /**
     * 转关键字数组
     * @param content
     * @return
     */
    public List<ApiKeywordContentDO> getKeywordList(String content){
        List<ApiKeywordContentDO> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(content)){
            JSONArray array = JSONArray.parseArray(content);
            if (array!=null && array.size()>0){
                for(int i=0;i<array.size();i++){
                    JSONObject obj = array.getJSONObject(i);
                    ApiKeywordContentDO dto = new ApiKeywordContentDO();
                    dto.setName(CommonUtils.formatString(obj.getString("name")));
                    dto.setStartPosition(CommonUtils.formatString(obj.getString("startPosition")));
                    dto.setEndPosition(CommonUtils.formatString(obj.getString("endPosition")));
                    dto.setUrl(CommonUtils.formatString(obj.getString("url")));
                    list.add(dto);
                }
            }
        }
        return list;
    }

    /**
     * 报告列表
     *
     * @return
     */
    @PostMapping(value = "/report/list")
    @ResponseBody
    public ResponseResult reportList(HttpServletRequest request,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        QuestionRecordPageDTO pageDTO = new QuestionRecordPageDTO();
        pageDTO.setUserId(member.getId());
        pageDTO.setPageNo(pageNo);
        pageDTO.setPageSize(pageSize);
        PageResult<QuestionRecordRespDTO> page = questionRecordApi.getQuestionRecordPage(pageDTO);
        if (page!=null && page.getList()!=null && page.getList().size()>0) {
            //拼接用户信息
            Set<Long> userIds = CollectionUtils.convertSet(page.getList(), QuestionRecordRespDTO::getUserId);
            if (userIds != null && userIds.size() > 0) {
                List<MemberUserDO> userList = memberUserService.getUserList(userIds);
                if (userList != null && userList.size() > 0) {
                    Map<Long, MemberUserDO> userMap = CollectionUtils.convertMap(userList, MemberUserDO::getId);
                    for (QuestionRecordRespDTO dd : page.getList()) {
                        if (dd.getUserId() != null && userMap.containsKey(dd.getUserId())) {
                            MemberUserDO userDO = userMap.get(dd.getUserId());
                            dd.setNickname(userDO.getNickname());
                            dd.setMobile(userDO.getMobile());
                        }
                    }
                }
            }

            Set<Long> questionIds = CollectionUtils.convertSet(page.getList(), QuestionRecordRespDTO::getQuestionId);
            if (questionIds != null && questionIds.size() > 0) {
                List<QuestionDTO> questionList = questionApi.getQuestionList(questionIds);
                if (questionList != null && questionList.size() > 0) {
                    Map<Long,QuestionDTO> questionMap = CollectionUtils.convertMap(questionList,QuestionDTO::getId);
                    for(QuestionRecordRespDTO dd:page.getList()) {
                        if (dd.getQuestionId() != null && questionMap.containsKey(dd.getQuestionId())) {
                            QuestionDTO questionDTO = questionMap.get(dd.getQuestionId());
                            dd.setQuestionName(questionDTO.getName());
                        }
                    }
                }
            }


            for (QuestionRecordRespDTO dd : page.getList()) {
                //格式化时间
                dd.setCreateTimeFormat(CommonUtils.formatLocalDateTime(dd.getCreateTime(), "yyyy-MM-dd HH:mm"));
                dd.setStartTimeFormat(CommonUtils.formatLocalDateTime(dd.getStartTime(), "yyyy-MM-dd HH:mm"));
                dd.setEndTimeFormat(CommonUtils.formatLocalDateTime(dd.getEndTime(), "yyyy-MM-dd HH:mm"));
            }
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(page);
        return rr;
    }

    /**
     * 报告详细
     *
     * @return
     */
    @PostMapping(value = "/report/detail")
    @ResponseBody
    public ResponseResult reportDetail(HttpServletRequest request,Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
//        if (member == null) {
//            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
//        }

        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数");
            return rr;
        }

        QuestionRecordRespDTO data = questionRecordApi.getQuestionRecord(id);
        if (data==null || (member!=null && !data.getUserId().equals(member.getId()))){
            return new ResponseResult(ResponseResult.ERROR, "记录不存在", null);
        }

        if (data.getUserId()!=null){
            MemberUserDO userDO = memberUserService.getUser(data.getUserId());
            if (userDO!=null){
                data.setNickname(userDO.getNickname());
                data.setMobile(userDO.getMobile());
            }
        }

        if (data.getQuestionId()!=null){
            QuestionDTO questionDO = questionApi.getQuestion(data.getQuestionId());
            if (questionDO!=null){
                data.setQuestionName(questionDO.getName());
            }
        }

        //格式化时间
        data.setCreateTimeFormat(CommonUtils.formatLocalDateTime(data.getCreateTime(), "yyyy-MM-dd HH:mm"));
        data.setStartTimeFormat(CommonUtils.formatLocalDateTime(data.getStartTime(), "yyyy-MM-dd HH:mm"));
        data.setEndTimeFormat(CommonUtils.formatLocalDateTime(data.getEndTime(), "yyyy-MM-dd HH:mm"));

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(data);
        return rr;
    }

    /**
     * 报告删除
     *
     * @return
     */
    @PostMapping(value = "/report/delete")
    @ResponseBody
    public ResponseResult reportDelete(HttpServletRequest request,Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数");
            return rr;
        }

        QuestionRecordRespDTO data = questionRecordApi.getQuestionRecord(id);
        if (data==null || !data.getUserId().equals(member.getId())){
            return new ResponseResult(ResponseResult.ERROR, "记录不存在", null);
        }

        try{
            questionRecordApi.deleteQuestionRecord(id);
            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            return rr;
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseResult(ResponseResult.ERROR , "操作失败" , null);
        }
    }

    /**
     * 根据值返回对应Obj信息
     * @param jsonArray
     * @param value
     * @return
     */
    public JSONObject getJSONObjectByValue(String jsonArray,String key,String value){
        JSONObject result = null;
        if (StringUtils.isNotEmpty(jsonArray)){
            try{
                JSONArray optArray = JSONArray.parseArray(jsonArray);
                if (optArray!=null && optArray.size()>0){
                     for(int i=0;i<optArray.size();i++){
                         JSONObject obj = optArray.getJSONObject(i);
                         if (obj.containsKey(key) && obj.getString(key).equalsIgnoreCase(value)){
                             result = obj;
                             break;
                         }
                     }
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }

        return result;
    }


    /**
     * 内容校验
     * @return
     */
    @RequestMapping(value = "/msg_sec_check")
    @ResponseBody
    public ResponseResult msgSecCheck(HttpServletRequest request,String content,Integer scene,
                                      String title,String nickname,String signature) {
        ResponseResult rr = new ResponseResult();

        MemberUserDO member = getCurrentUser(request);
        if (member == null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (StringUtils.isEmpty(content)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少内容参数");
            return rr;
        }
        String openid = getUserOpenId(request);
        if (StringUtils.isEmpty(openid)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("登录失效，请重新登录");
            return rr;
        }

        if (scene==null){
            scene =1 ;
        }

        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("获取accessToken失败");
            return rr;
        }

        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+accessToken;
        try {
            JSONObject entity = new JSONObject();
            entity.put("content", content);
            entity.put("scene", scene);
            entity.put("openid", openid);
            entity.put("version", 2);
            entity.put("title", title);
            entity.put("nickname", nickname);
            entity.put("signature", signature);

            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(entity.toJSONString(), "UTF-8");
            stringEntity.setContentType("application/json;charset=UTF-8");
            httpPost.setEntity(stringEntity);

            String result = HttpClientUtils.executeRequest(httpPost, "UTF-8");
            logger.info("msg_sec_check_result:{}",result);
            if (StringUtils.isNotEmpty(result)){
                JSONObject object = JSONObject.parseObject(result);
                if (object.containsKey("errcode") && object.getString("errcode").equalsIgnoreCase("0")){
                    JSONObject resObj = object.getJSONObject("result");
                    String label = resObj.getString("label");
                    if (label.equalsIgnoreCase("100")){
                        rr.setCode(ResponseResult.SUCCESS);
                        rr.setMessage("操作成功");
                        rr.setData(resObj.getString("suggest"));
                        return rr;
                    }else{
                        rr.setCode(ResponseResult.ERROR);
                        rr.setMessage("操作失败");
                        rr.setData(resObj.getString("suggest"));
                        return rr;
                    }
                }else{
                    rr.setCode(ResponseResult.ERROR);
                    rr.setMessage("操作失败");
                    rr.setData(object.getString("errmsg"));
                    return rr;
                }
            }
        }catch(Exception e) {
            logger.error(e.getMessage());
        }

        rr.setCode(ResponseResult.ERROR);
        rr.setMessage("操作失败");
        return rr;
    }


//    /**
//     * 获得个人签到统计
//     * @return
//     */
//    @RequestMapping("/sign/summary")
//    @ResponseBody
//    public ResponseResult getSignInRecordSummary(HttpServletRequest request, PageParam pageVO){
//        ResponseResult rr = new ResponseResult();
//        MemberUserDO member = getCurrentUser(request);
//        if (member==null) {
//            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
//        }
//
//        Map<String,Object> resultMap = new HashMap<>();
//        AppMemberSignInRecordSummaryRespVO respVO = signInRecordService.getSignInRecordSummary(member.getId());
//        Integer continuousDay = respVO.getContinuousDay();
//        Boolean todaySignIn = respVO.getTodaySignIn();
//        resultMap.put("totalDay",respVO.getTotalDay());
//        resultMap.put("continuousDay",continuousDay);
//        resultMap.put("todaySignIn",todaySignIn);
//        //是否做了签到提醒
//        resultMap.put("isDoSignRemind",messageRemindService.isDoDaySignRemind(member.getId()));
//        List<MemberSignInConfigDO> result = signInConfigService.getSignInConfigList(CommonStatusEnum.ENABLE.getStatus());
//        List<ApiMemberSignDO> list = new ArrayList<>();
//        if (result!=null && result.size()>0){
//            list = BeanUtils.toBean(result,ApiMemberSignDO.class);
//            for(ApiMemberSignDO dd:list){
//                dd.setIsSign(false);
//                if (continuousDay.compareTo(dd.getDay())>=0){
//                    dd.setIsSign(true);
//                }
//            }
//        }
//
//        resultMap.put("list",list);
//        rr.setCode(ResponseResult.SUCCESS);
//        rr.setMessage("操作成功");
//        rr.setData(resultMap);
//        return rr;
//    }
//
//    /**
//     * 获取签到配置列表
//     * @return
//     */
//    @RequestMapping("/sign/config")
//    @ResponseBody
//    public ResponseResult getSignConfig(HttpServletRequest request, PageParam pageVO){
//        ResponseResult rr = new ResponseResult();
//        MemberUserDO member = getCurrentUser(request);
//        if (member==null) {
//            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
//        }
//
//        rr.setCode(ResponseResult.SUCCESS);
//        rr.setMessage("操作成功");
//        rr.setData(signInConfigService.getSignInConfigList(CommonStatusEnum.ENABLE.getStatus()));
//        return rr;
//    }
//
//    /**
//     * 签到
//     * @return
//     */
//    @RequestMapping("/sign/create")
//    @ResponseBody
//    public ResponseResult createSignInRecord(HttpServletRequest request){
//        ResponseResult rr = new ResponseResult();
//        MemberUserDO member = getCurrentUser(request);
//        if (member==null) {
//            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
//        }
//
//        try{
//            MemberSignInRecordDO recordDO = signInRecordService.createSignRecord(member.getId());
//            rr.setCode(ResponseResult.SUCCESS);
//            rr.setMessage("操作成功");
//            rr.setData(RestMemberConvert.convertSignInRecord(MemberSignInRecordConvert.INSTANCE.coverRecordToAppRecordVo(recordDO)));
//            return rr;
//        }catch (Exception e){
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage(e.getMessage());
//            return rr;
//        }
//    }
//
//    /**
//     * 获得用户积分记录分页
//     * @return
//     */
//    @RequestMapping("/point/page")
//    @ResponseBody
//    public ResponseResult getPointPage(HttpServletRequest request, PageParam pageVO){
//        ResponseResult rr = new ResponseResult();
//        MemberUserDO member = getCurrentUser(request);
//        if (member==null) {
//            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
//        }
//
//        rr.setCode(ResponseResult.SUCCESS);
//        rr.setMessage("操作成功");
//        rr.setData(RestMemberConvert.convertPointPage(pointRecordService.getPointRecordPage(member.getId(), pageVO)));
//        return rr;
//    }
//
//    /**
//     * 查看新人礼包状态
//     * @return
//     */
//    @RequestMapping("/new-user/status")
//    @ResponseBody
//    public ResponseResult newUserStatus(HttpServletRequest request){
//        ResponseResult rr = new ResponseResult();
//        Boolean isCanTakeTakeNewGiftPack = true;
//        MemberUserDO member = getCurrentUser(request);
//        if (member!=null) {
//            Boolean isNewUser = tradeOrderQueryService.isNewUser(member.getId());
//            isCanTakeTakeNewGiftPack = false;
//            if (isNewUser){
//                if (member.getIsTakeNewGiftPack()==null || !member.getIsTakeNewGiftPack()){
//                    isCanTakeTakeNewGiftPack = true;
//                }else{
//                    if (member.getPackEndTime()==null || member.getPackEndTime().isBefore(LocalDateTime.now())){
//                        isCanTakeTakeNewGiftPack = true;
//                    }
//                }
//            }
//        }
//
//        Map<String,Object> map = new HashMap<>();
//        map.put("isCanTakeTakeNewGiftPack",isCanTakeTakeNewGiftPack);
//
//        rr.setCode(ResponseResult.SUCCESS);
//        rr.setMessage("操作成功");
//        rr.setData(map);
//        return rr;
//    }
//
//
//    /**
//     * 订阅消息提醒
//     * @return
//     */
//    @RequestMapping("/message/remind")
//    @ResponseBody
//    public ResponseResult messageRemind(HttpServletRequest request,Integer bizType,Long bizId){
//        ResponseResult rr = new ResponseResult();
//        MemberUserDO member = getCurrentUser(request);
//        if (member==null) {
//            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
//        }
//
//
//        if (bizType==null){
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage("缺少业务类型");
//            return rr;
//        }
//
//        try{
//            MessageRemindSaveReqVO createReqVO = new MessageRemindSaveReqVO();
//            createReqVO.setUserId(member.getId());
//            createReqVO.setOpenid(getUserOpenId(request));
//            createReqVO.setBizId(bizId);
//            createReqVO.setBizType(bizType);
//            createReqVO.setIsRemind(false);
//            if (bizType.equals(1)){
//                //预约到货通知
//                if (bizId==null){
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("缺少业务ID");
//                    return rr;
//                }
//            }else if (bizType.equals(2)){
//                //新品开售提醒
//                if (bizId==null){
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("缺少业务ID");
//                    return rr;
//                }
//
//                ArticleDO articleDO = articleService.getArticle(bizId);
//                if (articleDO!=null){
//                    createReqVO.setRemindTime(articleDO.getPublishDate());
//                }
//            }else if (bizType.equals(3)){
//                //卡券到期提醒
//                if (bizId==null){
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("缺少业务ID");
//                    return rr;
//                }
//
//                CouponDO couponDO = couponService.getCoupon(bizId);
//                if (couponDO!=null){
//                    createReqVO.setRemindTime(CommonUtils.addLocalDateTimeByDay(couponDO.getValidEndTime(),-1));
//                }
//            }else if (bizType.equals(4)){
//                //订单发货提醒
//                if (bizId==null){
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("缺少业务ID");
//                    return rr;
//                }
//            }else if (bizType.equals(5)){
//                //预约活动开始提醒
//                if (bizId==null){
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("缺少业务ID");
//                    return rr;
//                }
//
////                DrawActivityDO drawActivityDO = drawActivityService.getDrawActivity(bizId);
////                if (drawActivityDO==null){
////                    rr.setCode(ResponseResult.ERROR);
////                    rr.setMessage("找不到该抽选活动");
////                    return rr;
////                }
////
////                if (drawActivityDO.getStartTime().isBefore(LocalDateTime.now())){
////                    rr.setCode(ResponseResult.ERROR);
////                    rr.setMessage("该抽选活动已经开始啦");
////                    return rr;
////                }
//
////                createReqVO.setRemindTime(drawActivityDO.getStartTime());
//            }else if (bizType.equals(6)){
////                //活动开奖通知
////                DrawActivityDO drawActivityDO = drawActivityService.getDrawActivity(bizId);
////                if (drawActivityDO==null){
////                    rr.setCode(ResponseResult.ERROR);
////                    rr.setMessage("找不到该抽选活动");
////                    return rr;
////                }
////                if (drawActivityDO.getDrawTime().isBefore(LocalDateTime.now())){
////                    rr.setCode(ResponseResult.ERROR);
////                    rr.setMessage("该抽选活动已经开奖始啦");
////                    return rr;
////                }
////                createReqVO.setRemindTime(drawActivityDO.getDrawTime());
//            }else if (bizType.equals(7)){
//                //直播开播提醒
//                if (bizId==null){
//                    rr.setCode(ResponseResult.ERROR);
//                    rr.setMessage("缺少业务ID");
//                    return rr;
//                }
//                ArticleDO articleDO = articleService.getArticle(bizId);
//                if (articleDO!=null){
//                    createReqVO.setRemindTime(CommonUtils.addLocalDateTimeByHours(articleDO.getPublishDate(),-2));
//                }
//            }else if (bizType.equals(8)){
//                //签到提醒
//                String todayTime = CommonUtils.formatLocalDateTime(LocalDateTime.now(),"yyyy-MM-dd 08:00:00");
//                createReqVO.setRemindTime(CommonUtils.addLocalDateTimeByDay(CommonUtils.toFormatLocalDateTime(todayTime,null),1));
//            }else {
//                rr.setCode(ResponseResult.ERROR);
//                rr.setMessage("业务类型错误");
//                return rr;
//            }
//
//            messageRemindService.createMessageRemind(createReqVO);
//
//            rr.setCode(ResponseResult.SUCCESS);
//            rr.setMessage("操作成功");
//            return rr;
//        }catch (Exception e){
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage(e.getMessage());
//            return rr;
//        }
//    }
}
