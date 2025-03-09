package cn.iocoder.yudao.server.controller.cart;

import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppCartAddReqVO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppCartListRespVO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppCartUpdateCountReqVO;
import cn.iocoder.yudao.module.trade.controller.app.cart.vo.AppCartUpdateSelectedReqVO;
import cn.iocoder.yudao.module.trade.service.cart.CartService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.convert.RestCartConvert;
import cn.iocoder.yudao.server.util.ResponseResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *购物车对外接口
 */
@Tag(name = "对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api/cart")
public class CartController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(CartController.class);

    @Resource
    private CartService cartService;

    /**
     * 购物车列表
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public ResponseResult cartList(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        AppCartListRespVO cartListRespVO = cartService.getCartList(member.getId());
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestCartConvert.convertList(cartListRespVO));
        return rr;
    }

    /**
     * 添加购物车商品
     * @return
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseResult addCart(HttpServletRequest request, AppCartAddReqVO addCountReqVO) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (addCountReqVO.getSkuId()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        if (addCountReqVO.getCount()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }

        try{
            cartService.addCart(member.getId(),addCountReqVO);
        }catch (Exception e){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage(e.getMessage());
            return rr;
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 立即购买
     * @return
     */
    @PostMapping(value = "/now-buy")
    @ResponseBody
    public ResponseResult nowBuy(HttpServletRequest request, AppCartAddReqVO addCountReqVO) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (addCountReqVO.getSkuId()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        if (addCountReqVO.getCount()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        try{
            Long cartId = cartService.addCart(member.getId(),addCountReqVO);
            AppCartUpdateCountReqVO updateReqVO = new AppCartUpdateCountReqVO();
            updateReqVO.setId(cartId);
            updateReqVO.setCount(addCountReqVO.getCount());
            cartService.updateCartCount(member.getId(),updateReqVO);

            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            rr.setData(cartId);
            return rr;
        }catch (Exception e){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage(e.getMessage());
            return rr;
        }
    }

    /**
     * 更新购物车商品数量
     * @return
     */
    @PostMapping(value = "/update-count")
    @ResponseBody
    public ResponseResult updateCartCount(HttpServletRequest request, AppCartUpdateCountReqVO updateReqVO) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (updateReqVO.getId()==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        if (updateReqVO.getCount()==null || updateReqVO.getCount().compareTo(1)<0){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("数量必须大于 0");
            return rr;
        }
        cartService.updateCartCount(member.getId(),updateReqVO);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 更新购物车商品选中
     * @return
     */
    @PostMapping(value = "/update-selected")
    @ResponseBody
    public ResponseResult updateCartSelected(HttpServletRequest request, String ids,Boolean selected) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (StringUtils.isEmpty(ids) || selected==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        List<Long> idList = new ArrayList<>();
        for(String ss:ids.split(",")){
            if (StringUtils.isNotEmpty(ss)){
                idList.add(Long.valueOf(ss));
            }
        }
        AppCartUpdateSelectedReqVO updateReqVO = new AppCartUpdateSelectedReqVO();
        updateReqVO.setIds(idList);
        updateReqVO.setSelected(selected);
        cartService.updateCartSelected(member.getId(),updateReqVO);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 删除购物车商品
     * @return
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseResult deleteCart(HttpServletRequest request, String ids) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (StringUtils.isEmpty(ids)){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("参数错误");
            return rr;
        }
        List<Long> idList = new ArrayList<>();
        for(String ss:ids.split(",")){
            if (StringUtils.isNotEmpty(ss)){
                idList.add(Long.valueOf(ss));
            }
        }
        cartService.deleteCart(member.getId(),idList);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 查询用户在购物车中的商品数量
     * @return
     */
    @RequestMapping(value = "/get-count")
    @ResponseBody
    public ResponseResult getCartCount(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(cartService.getCartCount(member.getId()));
        return rr;
    }
}
