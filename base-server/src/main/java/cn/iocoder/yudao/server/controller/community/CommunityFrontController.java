package cn.iocoder.yudao.server.controller.community;

import cn.iocoder.yudao.module.member.api.feedback.MemberFeedbackApi;
import cn.iocoder.yudao.module.member.api.feedback.dto.MemberFeedbackDto;
import cn.iocoder.yudao.module.member.service.community.CommunityService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 论坛文章管理
 */
@RestController
@RequestMapping("/rest/api/member/community")
public class CommunityFrontController extends BaseController {

    @Resource
    private CommunityService communityService;

    @PostMapping("/add")
    public ResponseResult add(HttpServletRequest request){
        return new ResponseResult(ResponseResult.SUCCESS , "操作成功" , null);
    }

}
