package cn.iocoder.yudao.server.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.module.infra.service.file.FileService;
import cn.iocoder.yudao.module.member.controller.admin.articlevisit.vo.ArticleVisitSaveReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.articlevisit.ArticleVisitService;
import cn.iocoder.yudao.module.member.service.messageremind.MessageRemindService;
import cn.iocoder.yudao.module.product.controller.admin.category.vo.ProductCategoryListReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.category.ProductCategoryDO;
import cn.iocoder.yudao.module.product.service.category.ProductCategoryService;
import cn.iocoder.yudao.module.product.service.sku.ProductSkuService;
import cn.iocoder.yudao.module.product.service.spu.ProductSpuService;
import cn.iocoder.yudao.module.promotion.controller.admin.article.vo.article.ArticlePageReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.BannerPageReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleCategoryDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.banner.BannerDO;
import cn.iocoder.yudao.module.promotion.service.article.ArticleCategoryService;
import cn.iocoder.yudao.module.promotion.service.article.ArticleService;
import cn.iocoder.yudao.module.promotion.service.banner.BannerService;
import cn.iocoder.yudao.module.system.api.question.QuestionApi;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionDTO;
import cn.iocoder.yudao.module.system.api.question.dto.QuestionPageReqDTO;
import cn.iocoder.yudao.module.system.api.questiontopic.QuestionTopicApi;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.notice.vo.NoticePageReqVO;
import cn.iocoder.yudao.module.system.convert.ip.AreaConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import cn.iocoder.yudao.module.system.dal.dataobject.notice.NoticeDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.dict.DictDataService;
import cn.iocoder.yudao.module.system.service.notice.NoticeService;
import cn.iocoder.yudao.server.convert.*;
import cn.iocoder.yudao.server.util.CommonUtils;
import cn.iocoder.yudao.server.util.ResponseResult;
import cn.iocoder.yudao.server.vo.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



/**
 *对外接口
 */
@Tag(name = "对外接口")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest/api")
public class RestController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(RestController.class);

    @Resource
    private ProductSpuService productSpuService;
    @Resource
    private BannerService bannerService;
    @Resource
    private ProductCategoryService categoryService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private DictDataService dictDataService;
    @Resource
    private ArticleCategoryService articleCategoryService;
    @Resource
    private ArticleService articleService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private FileService fileService;
    @Resource
    private MessageRemindService messageRemindService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private QuestionApi questionApi;

    @Resource
    private QuestionTopicApi questionTopicApi;
    @Resource
    private DeptService deptService;
    @Resource
    private ArticleVisitService articleVisitService;

    /**
     * 省份地区
     *
     * @return
     */
    @RequestMapping(value = "/area/province")
    @ResponseBody
    public ResponseResult getProvinceArea(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(AreaUtils.getAreaListByParent(StrUtil.toString(Area.ID_CHINA)));
        return rr;
    }

    /**
     * 市/区地区
     *
     * @return
     */
    @RequestMapping(value = "/area/children")
    @ResponseBody
    public ResponseResult getProvinceArea(HttpServletRequest request, Integer id) {
        ResponseResult rr = new ResponseResult();
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(AreaUtils.getAreaListByParent(StrUtil.toString(id)));
        return rr;
    }

    /**
     * 广告查询
     * @return
     */
    @RequestMapping(value = "/ad/list")
    @ResponseBody
    public ResponseResult adList(HttpServletRequest request,Integer position) {
        ResponseResult rr = new ResponseResult();
        BannerPageReqVO pageVO = new BannerPageReqVO();
        if (position==null){
            position=1;
        }

        List<BannerDO> bannerDOList = bannerService.getBannerListByPosition(position);
        if (bannerDOList == null || bannerDOList.isEmpty()) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("广告不存在");
            return rr;
        }

        boolean isAllEnabled = bannerDOList.stream().allMatch(banner -> CommonStatusEnum.isEnable(banner.getStatus()));
        if (!isAllEnabled) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("广告不存在");
            return rr;
        }

        List<ApiBannerDO> apiBannerDOList = RestBannerConvert.convertToList(bannerDOList);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(apiBannerDOList);
        return rr;
    }

    /**
     * 工会查询
     * @return
     */
    @PostMapping(value = "/dept/list")
    @ResponseBody
    public ResponseResult deptList(HttpServletRequest request,String name) {
        ResponseResult rr = new ResponseResult();
        DeptPageReqVO pageVO = new DeptPageReqVO();
        pageVO.setName(name);
        PageResult<ApiDeptDO> page = RestDeptConvert.convert(deptService.getDeptPage(pageVO));
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(page);
        return rr;
    }

    /**
     * 产品分类查询
     * @return
     */
    @PostMapping(value = "/category/list")
    @ResponseBody
    public ResponseResult categoryList(HttpServletRequest request,Long parentId) {
        ResponseResult rr = new ResponseResult();
        if (parentId==null){
            parentId=0L;
        }
        ProductCategoryListReqVO reqVO = new ProductCategoryListReqVO();
        reqVO.setParentId(parentId);
        List<ProductCategoryDO> list = categoryService.getEnableCategoryList(reqVO);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(list);
        return rr;
    }

    /**
     * 文章分类
     * @return
     */
    @PostMapping(value = "/article/category/list")
    @ResponseBody
    public ResponseResult articleCategoryList(HttpServletRequest request,Integer status,String orderBy,Long parentId) {
        ResponseResult rr = new ResponseResult();
        List<ArticleCategoryDO> list = articleCategoryService.findCategoryList(status,orderBy,parentId);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestArticleConvert.convert(list));
        return rr;
    }

    /**
     * 文章列表
     * @return
     */
    @PostMapping(value = "/article/list")
    @ResponseBody
    public ResponseResult articleList(HttpServletRequest request,
                                      Long categoryId,String keyword,Integer status,String orderBy,Long parentId,Integer ifExhibition,
                                      @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        if (status==null){
            status = CommonStatusEnum.ENABLE.getStatus();
        }

        ResponseResult rr = new ResponseResult();
        if (categoryId!=null){
            List<ArticleDO> articlesByCategoryId = articleService.getArticleByCategoryId(categoryId);
            if (articlesByCategoryId == null || articlesByCategoryId.isEmpty()) {
                rr.setCode(ResponseResult.ERROR);
                rr.setMessage("分类编号不存在");
                return rr;
            }
        }
        
        ArticlePageReqVO pageVO = new ArticlePageReqVO();
        pageVO.setPageNo(pageNo);
        pageVO.setPageSize(pageSize);
        pageVO.setCategoryId(categoryId);
        pageVO.setTitle(keyword);
        pageVO.setParentId(parentId);
        if (!status.equals(-1)){
            pageVO.setStatus(status);
        }
        if (ifExhibition==null){
            ifExhibition = 0;
        }
        if (!ifExhibition.equals(-1)){
            pageVO.setIfExhibition(ifExhibition);
        }
        pageVO.setOrderBy(orderBy);

        PageResult<ApiArticleDO> page = RestArticleConvert.convert(articleService.getArticlePage(pageVO));
        if (page!=null && page.getList()!=null && page.getList().size()>0){
            Set<Long> categoryIds = CollectionUtils.convertSet(page.getList(), ApiArticleDO::getCategoryId);
            if (categoryIds!=null && categoryIds.size()>0){
                List<ArticleCategoryDO> categoryDOList = articleCategoryService.getCategoryListByIds(categoryIds);
                Map<Long, ArticleCategoryDO> categoryMap = CollectionUtils.convertMap(categoryDOList,ArticleCategoryDO::getId);
                for(ApiArticleDO dd:page.getList()){
                    ArticleCategoryDO categoryDO = categoryMap.get(dd.getCategoryId());
                    if (categoryDO!=null){
                        dd.setCategoryName(categoryDO.getName());
                        dd.setRemark(categoryDO.getRemark());
                    }
                }
            }
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(page);
        return rr;
    }

    /**
     * 文章详细
     * @param request
     * @param id
     * @return
     */
    @PostMapping(value = "/article/detail")
    @ResponseBody
    public ResponseResult articleDetail(HttpServletRequest request, Long id,Boolean isNeedPreviousAndNext,String orderBy) {
        ResponseResult rr = new ResponseResult();
        MemberUserDO member = getCurrentUser(request);
        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数id");
            return rr;
        }

        ArticleDO articleDO = articleService.getArticle(id);
        if (articleDO==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("文章不存在");
            return rr;
        }

        if(!CommonStatusEnum.isEnable(articleDO.getStatus())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("文章不存在");
            return rr;
        }
        //获取分类
        ArticleCategoryDO categoryDO = articleCategoryService.getArticleCategory(articleDO.getCategoryId());

        //更新点击数
        Long hitCount = articleDO.getHitCount();
        if (hitCount==null){
            hitCount=0L;
        }
        hitCount=hitCount+1;
        articleDO.setHitCount(hitCount);
        articleService.updateArticle(articleDO);
        //插入点击流水
        ArticleVisitSaveReqVO saveReqVO = new ArticleVisitSaveReqVO();
        saveReqVO.setArticleId(articleDO.getId());
        saveReqVO.setArticleTitle(articleDO.getTitle());
        saveReqVO.setArticleCategoryId(articleDO.getCategoryId());
        if (categoryDO!=null){
            saveReqVO.setArticleCategory(categoryDO.getName());
        }
        if (member!=null){
            saveReqVO.setUserId(member.getId());
            saveReqVO.setUserName(member.getNickname());
            saveReqVO.setUserMobile(member.getMobile());
        }
        articleVisitService.createArticleVisit(saveReqVO);

        ApiArticleDO resData = RestArticleConvert.convert(articleDO);
        if (categoryDO!=null){
            resData.setCategoryName(categoryDO.getName());
            resData.setRemark(categoryDO.getRemark());
        }
        if (isNeedPreviousAndNext!=null && isNeedPreviousAndNext){
            resData.setPreviousArticle(RestArticleConvert.convert(articleService.getPreviousArticle(resData.getId(),resData.getCategoryId(),orderBy)));
            resData.setNextArticle(RestArticleConvert.convert(articleService.getNextArticle(resData.getId(),resData.getCategoryId(),orderBy)));
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(resData);
        return rr;
    }


    /**
     * 文章列表(按发布时间分组)
     * @return
     */
    @RequestMapping(value = "/article/list-group")
    @ResponseBody
    public ResponseResult articleListGroup(HttpServletRequest request, Long categoryId) {
        ResponseResult rr = new ResponseResult();
        if (categoryId==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数categoryId");
            return rr;
        }
        List<ArticleDO> list = articleService.getEnabledArticleByCategoryId(categoryId);
        List<ApiArticleGroupDO> resList = RestArticleConvert.convertGroup(list);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(resList);
        return rr;
    }

    /**
     * 通知内容
     * @return
     */
    @RequestMapping(value = "/notice")
    @ResponseBody
    public ResponseResult noticeContent(HttpServletRequest request, Integer status, Long id, Integer type,
                                        @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ResponseResult rr = new ResponseResult();
        NoticePageReqVO pageVO = new NoticePageReqVO();

        pageVO.setPageNo(pageNo);
        pageVO.setPageSize(pageSize);

        if (status == null) {
            status = CommonStatusEnum.ENABLE.getStatus();
        }
        pageVO.setStatus(status);
        if (id!=null){
            pageVO.setId(id);
        }

        PageResult<NoticeDO> page = noticeService.getNoticePage(pageVO);
        if (page == null ) {
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("公告不存在");
            return rr;
        }

        PageResult<ApiNoticeDO> resPage = RestNoticeConvert.convert(page);
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(resPage);
        return rr;
    }
    /**
     * 公告详细
     * @return
     */
    @RequestMapping(value = "/notice/detail")
    @ResponseBody
    public ResponseResult noticeList(HttpServletRequest request, Long id) {
        ResponseResult rr = new ResponseResult();
        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数id");
            return rr;
        }

        NoticeDO noticeDO = noticeService.getNotice(id);
        if (noticeDO==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("公告不存在");
            return rr;
        }

        if(!CommonStatusEnum.isEnable(noticeDO.getStatus())){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("公告不存在");
            return rr;
        }

        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestNoticeConvert.convert(noticeDO));
        return rr;
    }


    /**
     * 上传文件
     * @return
     */
    @RequestMapping(value = "/file/upload")
    @ResponseBody
    public ResponseResult uploadFile(HttpServletRequest request, MultipartFile file) {
        ResponseResult rr = new ResponseResult();

        MemberUserDO member = getCurrentUser(request);
        if (member==null){
            rr.setCode(ResponseResult.NOLOGIN);
            rr.setMessage("还未登录");
            return rr;
        }

        if (file==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("获取不到上传文件");
            return rr;
        }

        try{
            String url = fileService.createFile(file.getOriginalFilename(), null, IoUtil.readBytes(file.getInputStream()));
            rr.setCode(ResponseResult.SUCCESS);
            rr.setMessage("操作成功");
            rr.setData(url);
            return rr;
        }catch (Exception e){
            logger.error(e.getMessage());
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("上传文件失败");
            return rr;
        }
    }

    /**
     * 上传Excel
     * @return
     */
    @RequestMapping(value = "/excel/upload")
    @ResponseBody
    public ResponseResult uploadExcel(HttpServletRequest request, MultipartFile file,String type) {
        ResponseResult rr = new ResponseResult();

        MemberUserDO member = getCurrentUser(request);
        if (member==null){
            rr.setCode(ResponseResult.NOLOGIN);
            rr.setMessage("还未登录");
            return rr;
        }

        if (file==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("获取不到上传文件");
            return rr;
        }

//        try{
//            //团体成员导入
//            if (type.equalsIgnoreCase("group_person_import")){
//                List<GroupSubVo> list = ExcelUtils.read(file, GroupSubVo.class);
//                rr.setCode(ResponseResult.SUCCESS);
//                rr.setMessage("操作成功");
//                rr.setData(list);
//                return rr;
//            }
//        }catch (Exception e){
//            logger.error(e.getMessage());
//        }
        rr.setCode(ResponseResult.ERROR);
        rr.setMessage("操作失败");
        return rr;
    }


    /**
     * 数据字典查询
     * @return
     */
    @RequestMapping(value = "/dict-data/list")
    @ResponseBody
    public ResponseResult adictDataList(HttpServletRequest request,String dictType) {
        ResponseResult rr = new ResponseResult();

        List<DictDataDO> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(dictType)){
            list = dictDataService.getDictDataList(CommonStatusEnum.ENABLE.getStatus(),dictType);
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(RestDictDataConvert.convert(list));
        return rr;
    }

    /**
     * 地区
     * @return
     */
    @RequestMapping(value = "/area/tree")
    @ResponseBody
    public ResponseResult getAreaTree(HttpServletRequest request) {
        ResponseResult rr = new ResponseResult();

        Area area = AreaUtils.getArea(Area.ID_CHINA);
        if(area==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("获取不到中国地区");
            return rr;
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(AreaConvert.INSTANCE.convertList3(area.getChildren()));
        return rr;
    }


//    /**
//     * 产品查询
//     * @return
//     */
//    @RequestMapping(value = "/product/search")
//    @ResponseBody
//    public ResponseResult productSearch(HttpServletRequest request,String type, Long categoryId,String keyword,
//                                        @RequestParam(required = false, defaultValue = "1") Integer pageNo,
//                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
//        ResponseResult rr = new ResponseResult();
//        //首页推荐产品
//        if (StringUtils.isBlank(type)){
//            type="0";
//        }
//
//        if (type.equalsIgnoreCase("0")){
//            //首页推荐产品
//            PageResult<ProductUnionVo> page =  productSpuService.getUnionProductPage(pageNo,pageSize,keyword,false);
//            rr.setCode(ResponseResult.SUCCESS);
//            rr.setMessage("操作成功");
//            rr.setData(RestProductConvert.convertProductUnionPage(page));
//            return rr;
//        }else if (type.equalsIgnoreCase("2")){
//            if (categoryId==null){
//                categoryId=CATEGORY_COMMON_ID;
//            }
//            //会员购查询
//            AppProductSpuPageReqVO pageReqVO = new AppProductSpuPageReqVO();
//            pageReqVO.setKeyword(keyword);
//            pageReqVO.setIsFirstProduct(false);
//            pageReqVO.setPageNo(pageNo);
//            pageReqVO.setPageSize(pageSize);
//            pageReqVO.setCategoryId(categoryId);
//            pageReqVO.setSortField(AppProductSpuPageReqVO.RECOMMEND_TYPE_HOT);
//            PageResult<ProductSpuDO> page = productSpuService.getSpuPage(pageReqVO);
//            rr.setCode(ResponseResult.SUCCESS);
//            rr.setMessage("操作成功");
//            rr.setData(RestProductConvert.convertProductSpuPage(page));
//            return rr;
//        }else if (type.equalsIgnoreCase("3")){
//            //新人首单优惠产品
//            PageResult<ProductUnionVo> page =  productSpuService.getUnionProductPage(pageNo,pageSize,keyword,true);
//            rr.setCode(ResponseResult.SUCCESS);
//            rr.setMessage("操作成功");
//            rr.setData(RestProductConvert.convertProductUnionPage(page));
//            return rr;
//        }
//
//        rr.setCode(ResponseResult.ERROR);
//        rr.setMessage("操作失败");
//        return rr;
//    }
//
//
//    /**
//     * 普通商品详细
//     * @return
//     */
//    @RequestMapping(value = "/product/detail")
//    @ResponseBody
//    public ResponseResult productDetail(HttpServletRequest request,Long id) {
//        ResponseResult rr = new ResponseResult();
//
//        if (id==null){
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage("缺少参数id");
//            return rr;
//        }
//
//        ProductSpuDO product = productSpuService.getSpu(id);
//        if (product==null){
//            rr.setCode(ResponseResult.ERROR);
//            rr.setMessage("产品不存在或已下架");
//            return rr;
//        }
//
//        // 查询商品 SKU
//        List<ProductSkuDO> skus = productSkuService.getSkuListBySpuId(product.getId());
//        rr.setCode(ResponseResult.SUCCESS);
//        rr.setMessage("操作成功");
//        ApiProductDetailDO dto = RestProductConvert.convert(product,skus);
//        dto.setIsCanBuy(true);
//        //判断是否为特殊商品
//        if (isSpecSpu(product.getCategoryId())){
//            dto.setIsCanBuy(false);
//        }
//        if (product.getGiveGift()!=null){
//            ProductSpuDO gift = productSpuService.getSpu(product.getGiveGift());
//            if (gift!=null){
//                dto.setGiveGift(BeanUtils.toBean(gift, ApiGiftDO.class));
//            }
//        }
//        rr.setData(dto);
//        return rr;
//    }
//
//    /**
//     * 判断是否为特殊商品
//     * @return
//     */
//    public Boolean isSpecSpu(Long categoryId){
//        Boolean result = false;
//        if (categoryId!=null){
//            List<Long> parentIdList = new ArrayList<>();
//            parentIdList.add(CATEGORY_SPEC_ID);
//            List<ProductCategoryDO> categoryChildren = categoryService.getEnableCategoryList(parentIdList);
//            if (categoryChildren!=null){
//                for(ProductCategoryDO dd:categoryChildren){
//                    if (dd.getId().equals(categoryId)){
//                        result = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        return result;
//    }



    /**
     * 页面访问日志
     * @return
     */
    @RequestMapping(value = "/log/visit/add")
    @ResponseBody
    public ResponseResult logVisitAdd(HttpServletRequest request,String type,String title,String path,String groupName,String bizId) {
        ResponseResult rr = new ResponseResult();
        String traceId = System.currentTimeMillis()+""+ RandomUtil.randomNumbers(3);
        try{
            if (StringUtils.isEmpty(type)){
                type = "1";
            }
            String userId = CommonUtils.formatLong(getUserId(request));
            String requestUrl = CommonUtils.formatString(path);
            title = CommonUtils.decryptEnCode(title);
            String userAgent = CommonUtils.formatString(ServletUtils.getUserAgent(request));
            String userIp = CommonUtils.formatString(ServletUtils.getClientIP(request));

            String beginTimeStr = CommonUtils.formatLocalDateTime(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss");
            String mobileModel = CommonUtils.getMobileModel(userAgent);

            groupName = CommonUtils.decryptEnCode(groupName);
            bizId = CommonUtils.formatString(bizId);

            String content = type+"|"+traceId+"|"+userId+"|"+requestUrl+"|"+title+"|"
                    +userAgent+"|"+userIp+"|"+beginTimeStr+"|"+mobileModel+"|"+groupName+"|"+bizId;
            writeLog(content);

//            ApiAccessLogCreateReqDTO accessLog = new ApiAccessLogCreateReqDTO();
//            accessLog.setApplicationName(applicationName);
//            accessLog.setUserId(getUserId(request));
//            accessLog.setUserType(UserTypeEnum.MEMBER.getValue());
//            accessLog.setRequestUrl(path);
//            accessLog.setTitle(title);
//            accessLog.setUserAgent(ServletUtils.getUserAgent(request));
//            accessLog.setUserIp(ServletUtils.getClientIP(request));
//            // 持续时间
//            accessLog.setBeginTime(LocalDateTime.now());
//            accessLog.setTraceId(TracerUtils.getTraceId());
//            accessLog.setResultCode(0);
//            accessLog.setResultMsg("");
//            accessLog.setType(1);
//            accessLog.setMobileModel(CommonUtils.getMobileModel(accessLog.getUserAgent()));
//            apiAccessLogApi.createApiAccessLog(accessLog);
        }catch (Exception e){
            logger.error(e.getMessage());
            traceId = null;
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(traceId);
        return rr;
    }

    /**
     * 页面访问结束日志
     * @return
     */
    @RequestMapping(value = "/log/visit/end")
    @ResponseBody
    public ResponseResult logVisitEnd(HttpServletRequest request,String id,String type) {
        ResponseResult rr = new ResponseResult();
        if (id!=null){
            if (StringUtils.isEmpty(type)){
                type = "2";
            }
            String content = type+"|"+id+"|"+CommonUtils.formatLocalDateTime(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss");
            writeLog(content);
//            redisTemplate.opsForList().leftPush(REDIS_KEY_LOG_LIST,content);
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        return rr;
    }

    /**
     * 量表列表查询
     * @return
     */
    @RequestMapping(value = "/question/list")
    @ResponseBody
    public ResponseResult getQuestionList(HttpServletRequest request,
                                          Integer type,
                                          @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ResponseResult rr = new ResponseResult();

        QuestionPageReqDTO pageReqDTO = new QuestionPageReqDTO();
        pageReqDTO.setType(type);
        pageReqDTO.setPageNo(pageNo);
        pageReqDTO.setPageSize(pageSize);
        pageReqDTO.setStatus(CommonStatusEnum.ENABLE.getStatus());

        PageResult<QuestionDTO> page = questionApi.getQuestionPage(pageReqDTO);
        if (page!=null && page.getList()!=null && page.getList().size()>0){
            for(QuestionDTO dd:page.getList()){
                dd.setStartTimeFormat(CommonUtils.formatLocalDateTime(dd.getStartTime(),"yyyy-MM-dd"));
                dd.setEndTimeFormat(CommonUtils.formatLocalDateTime(dd.getEndTime(),"yyyy-MM-dd"));
                dd.setCreateTimeFormat(CommonUtils.formatLocalDateTime(dd.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            }
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(page);
        return rr;
    }


    /**
     * 量表详细
     * @return
     */
    @RequestMapping(value = "/question/info")
    @ResponseBody
    public ResponseResult getQuestionInfo(HttpServletRequest request,Long id) {
        ResponseResult rr = new ResponseResult();
        if (id==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数id");
            return rr;
        }
        QuestionDTO dto = questionApi.getQuestion(id);
        if (dto!=null){
            dto.setStartTimeFormat(CommonUtils.formatLocalDateTime(dto.getStartTime(),"yyyy-MM-dd"));
            dto.setEndTimeFormat(CommonUtils.formatLocalDateTime(dto.getEndTime(),"yyyy-MM-dd"));
            dto.setCreateTimeFormat(CommonUtils.formatLocalDateTime(dto.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(dto);
        return rr;
    }

    /**
     * 获取量表管理题目
     * @return
     */
    @RequestMapping(value = "/question/topic")
    @ResponseBody
    public ResponseResult getQuestionTopic(HttpServletRequest request,Long questionId,String topicCode,Integer currentNo,String orderBy) {
        ResponseResult rr = new ResponseResult();
        if (questionId==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数questionId");
            return rr;
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(questionTopicApi.getQuestionTopic(questionId,topicCode,currentNo,orderBy));
        return rr;
    }

    /**
     * 获取量表管理题目列表
     * @return
     */
    @RequestMapping(value = "/question/topic/list")
    @ResponseBody
    public ResponseResult getQuestionTopicList(HttpServletRequest request,Long questionId) {
        ResponseResult rr = new ResponseResult();
        if (questionId==null){
            rr.setCode(ResponseResult.ERROR);
            rr.setMessage("缺少参数questionId");
            return rr;
        }
        rr.setCode(ResponseResult.SUCCESS);
        rr.setMessage("操作成功");
        rr.setData(questionTopicApi.getQuestionTopicListById(questionId));
        return rr;
    }

}
