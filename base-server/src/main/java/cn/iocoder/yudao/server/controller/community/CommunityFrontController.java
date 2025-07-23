package cn.iocoder.yudao.server.controller.community;

import cn.iocoder.yudao.module.member.api.feedback.MemberFeedbackApi;
import cn.iocoder.yudao.module.member.api.feedback.dto.MemberFeedbackDto;
import cn.iocoder.yudao.module.member.controller.admin.community.vo.CommunitySaveReqVO;
import cn.iocoder.yudao.module.member.service.community.CommunityService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import cn.iocoder.yudao.module.member.dal.dataobject.community.CommunityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.community.vo.CommunityPageReqVO;

/**
 * 论坛文章管理
 */
@RestController
@RequestMapping("/rest/api/member/community")
@Validated
public class CommunityFrontController extends BaseController {

    @Resource
    private CommunityService communityService;

    @PostMapping("/add")
    public ResponseResult add(@Valid @RequestBody CommunitySaveReqVO reqVO) {
        Long id = communityService.createCommunity(reqVO);
        return new ResponseResult(ResponseResult.SUCCESS, "操作成功", id);
    }

    @PostMapping("/update")
    public ResponseResult update(@Valid @RequestBody CommunitySaveReqVO reqVO) {
        communityService.updateCommunity(reqVO);
        return new ResponseResult(ResponseResult.SUCCESS, "操作成功", null);
    }

    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody Long id) {
        communityService.deleteCommunity(id);
        return new ResponseResult(ResponseResult.SUCCESS, "操作成功", null);
    }

    @PostMapping("/my-list")
    public ResponseResult myList(@RequestBody CommunityPageReqVO reqVO) {
        PageResult<CommunityDO> page = communityService.getCommunityPage(reqVO);
        return new ResponseResult(ResponseResult.SUCCESS, "操作成功", page);
    }
}
