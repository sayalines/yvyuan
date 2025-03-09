package cn.iocoder.yudao.server.controller.feedback;

import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.api.feedback.dto.MemberFeedbackDto;
import cn.iocoder.yudao.module.member.api.feedback.MemberFeedbackApi;
import cn.iocoder.yudao.module.system.api.sensitiveword.SensitiveWordApi;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 问题反馈
 */
@RestController
@RequestMapping("/rest/api/member/question")
public class FeedbackFrontController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(FeedbackFrontController.class);
    @Resource
    private MemberFeedbackApi memberFeedbackApi;
    @Resource
    private SensitiveWordApi sensitiveWordApi;

    @PostMapping("/add")
    public ResponseResult add(HttpServletRequest request, MemberFeedbackDto memberQuestionDto, String content, String mobile, String userName){
        MemberUserDO currentUser = getCurrentUser(request);
        if (currentUser==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (StringUtils.isEmpty(mobile)){
            return new ResponseResult(ResponseResult.ERROR, "缺少手机号", null);
        }
        if (StringUtils.isEmpty(userName)){
            return new ResponseResult(ResponseResult.ERROR, "缺少用户昵称", null);
        }
        if (StringUtils.isEmpty(content)){
            return new ResponseResult(ResponseResult.ERROR, "缺少内容参数", null);
        }
        boolean textValid = sensitiveWordApi.isTextValid(memberQuestionDto.getContent(), null);
        if (!textValid){
            return new ResponseResult(ResponseResult.ERROR, "内容中包含敏感字", null);
        }
        try {
            memberQuestionDto.setUserId(currentUser.getId());
            Integer add = memberFeedbackApi.add(memberQuestionDto);
            return new ResponseResult(ResponseResult.SUCCESS , "操作成功" , add == 1);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseResult(ResponseResult.ERROR , "操作失败" , null);
        }

    }
}
