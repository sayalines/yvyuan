package cn.iocoder.yudao.server.controller.address;

import cn.iocoder.yudao.module.member.controller.app.address.vo.AppAddressCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.address.vo.AppAddressUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.address.AddressConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.address.AddressService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.ResponseResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *收件地址 对外接口
 */
@Tag(name = "收件地址对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/address")
public class AddreeController extends BaseController {
    @Resource
    private AddressService addressService;
    /**
     * 创建用户收件地址
     * @return
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public ResponseResult createAddress(HttpServletRequest request, AppAddressCreateReqVO createReqVO) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (StringUtils.isEmpty(createReqVO.getName())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("收件人名称不能为空");
            return rr;
        }
        if (StringUtils.isEmpty(createReqVO.getMobile())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("手机号不能为空");
            return rr;
        }
        if (createReqVO.getAreaId()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("地区编号不能为空");
            return rr;
        }
        if (StringUtils.isEmpty(createReqVO.getDetailAddress())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("收件详细地址不能为空");
            return rr;
        }
        if (createReqVO.getDefaultStatus()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("是否默认地址不能为空");
            return rr;
        }

        addressService.createAddress(member.getId(),createReqVO);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 修改用户收件地址
     * @return
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public ResponseResult updateAddress(HttpServletRequest request, AppAddressUpdateReqVO updateReqVO) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (updateReqVO.getId()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("ID不能为空");
            return rr;
        }
        if (StringUtils.isEmpty(updateReqVO.getName())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("收件人名称不能为空");
            return rr;
        }
        if (StringUtils.isEmpty(updateReqVO.getMobile())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("手机号不能为空");
            return rr;
        }
        if (updateReqVO.getAreaId()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("地区编号不能为空");
            return rr;
        }
        if (StringUtils.isEmpty(updateReqVO.getDetailAddress())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("收件详细地址不能为空");
            return rr;
        }
        if (updateReqVO.getDefaultStatus()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("是否默认地址不能为空");
            return rr;
        }

        addressService.updateAddress(member.getId(),updateReqVO);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 删除用户收件地址
     * @return
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseResult deleteAddress(HttpServletRequest request,  Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("ID不能为空");
            return rr;
        }

        addressService.deleteAddress(member.getId(),id);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 获得用户收件地址
     * @return
     */
    @RequestMapping(value = "/get")
    @ResponseBody
    public ResponseResult getAddress(HttpServletRequest request,  Long id) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("ID不能为空");
            return rr;
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(AddressConvert.INSTANCE.convert(addressService.getAddress(member.getId(),id)));
        return rr;
    }

    /**
     * 获得用户默认收件地址
     * @return
     */
    @RequestMapping(value = "/get-default")
    @ResponseBody
    public ResponseResult getDefaultUserAddress(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(AddressConvert.INSTANCE.convert(addressService.getDefaultUserAddress(member.getId())));
        return rr;
    }

    /**
     * 获得用户收件地址列表
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public ResponseResult getAddressList(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(AddressConvert.INSTANCE.convertList(addressService.getAddressList(member.getId())));
        return rr;
    }
}
