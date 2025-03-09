package cn.iocoder.yudao.server.controller.favorite;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.product.controller.app.favorite.vo.AppFavoritePageReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.module.product.service.favorite.ProductFavoriteService;
import cn.iocoder.yudao.module.product.service.spu.ProductSpuService;
import cn.iocoder.yudao.server.controller.BaseController;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.util.ResponseResult;
import cn.iocoder.yudao.server.vo.ApiProductFavoriteDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * 商品收藏
 */
@RestController
@RequestMapping("/rest/api/member/favorite")
public class FavoriteFrontController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(FavoriteFrontController.class);
    @Resource
    private ProductFavoriteService productFavoriteService;
    @Resource
    private ProductSpuService productSpuService;

    /**
     * 商品收藏
     * @return
     */
    @PostMapping("/create")
    @ResponseBody
    public ResponseResult create(HttpServletRequest request,Long spuId){
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (spuId==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数");
            return rr;
        }

        ProductSpuDO spu = productSpuService.getSpu(spuId);
        if (spu==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("商品不存在");
            return rr;
        }

        Long dataId = productFavoriteService.createFavorite(member.getId(),spuId);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(dataId);
        return rr;
    }

    /**
     * 取消商品收藏
     * @return
     */
    @PostMapping("/cancel")
    @ResponseBody
    public ResponseResult cancel(HttpServletRequest request,Long spuId){
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (spuId==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数");
            return rr;
        }

        //删除商品收藏
        productFavoriteService.deleteFavorite(member.getId(),spuId);

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 我的商品收藏
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public ResponseResult list(HttpServletRequest request,
                                             @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }

        AppFavoritePageReqVO pageReqVO = new AppFavoritePageReqVO();
        pageReqVO.setPageNo(pageNo);
        pageReqVO.setPageSize(pageSize);

        PageResult<ApiProductFavoriteDO> page = BeanUtils.toBean(productFavoriteService.getFavoritePage(member.getId(),pageReqVO),ApiProductFavoriteDO.class);
        if (page!=null && page.getList()!=null && page.getList().size()>0){
            //商品拼接
            Set<Long> idsSet = convertSet(page.getList(), ApiProductFavoriteDO::getSpuId);
            if (idsSet.size()>0){
                List<ProductSpuDO> spus = productSpuService.getSpuList(idsSet);
                Map<Long, ProductSpuDO> spuMap = convertMap(spus, ProductSpuDO::getId);
                page.getList().forEach(record -> MapUtils.findAndThen(spuMap, record.getSpuId(),
                        respDTO -> record.setSpuName(respDTO.getName()).setPicUrl(respDTO.getPicUrl())));
            }

            for(ApiProductFavoriteDO dd:page.getList()){
                dd.setCreateTimeFormat(CommonUtils.formatLocalDateTime(dd.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            }
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(page);
        return rr;
    }

    /**
     * 删除商品收藏
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseResult delete(HttpServletRequest request,Long id){
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (member==null) {
            return new ResponseResult(ResponseResult.NOLOGIN, "还未登录", null);
        }
        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数");
            return rr;
        }

        ProductFavoriteDO entity = productFavoriteService.getFavorite(id);
        if (entity!=null){
            //删除商品收藏
            productFavoriteService.deleteFavorite(entity.getId());
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }
}
