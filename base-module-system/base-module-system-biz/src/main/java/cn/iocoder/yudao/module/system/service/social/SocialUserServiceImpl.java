package cn.iocoder.yudao.module.system.service.social;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.yudao.module.system.controller.admin.socail.vo.user.SocialUserPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserBindDO;
import cn.iocoder.yudao.module.system.dal.dataobject.social.SocialUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialUserBindMapper;
import cn.iocoder.yudao.module.system.dal.mysql.social.SocialUserMapper;
import cn.iocoder.yudao.module.system.enums.social.SocialTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xingyuv.jushauth.model.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.json.JsonUtils.toJsonString;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.AUTH_THIRD_LOGIN_NOT_BIND;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.SOCIAL_USER_NOT_FOUND;

/**
 * 社交用户 Service 实现类
 *
 * @author 小猪出行
 */
@Service
@Validated
@Slf4j
public class SocialUserServiceImpl implements SocialUserService {

    @Resource
    private SocialUserBindMapper socialUserBindMapper;
    @Resource
    private SocialUserMapper socialUserMapper;

    @Resource
    private SocialClientService socialClientService;

    @Override
    public List<SocialUserDO> getSocialUserList(Long userId, Integer userType) {
        // 获得绑定
        List<SocialUserBindDO> socialUserBinds = socialUserBindMapper.selectListByUserIdAndUserType(userId, userType);
        if (CollUtil.isEmpty(socialUserBinds)) {
            return Collections.emptyList();
        }
        // 获得社交用户
        return socialUserMapper.selectBatchIds(convertSet(socialUserBinds, SocialUserBindDO::getSocialUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String bindSocialUser(SocialUserBindReqDTO reqDTO) {
        // 获得社交用户
        SocialUserDO socialUser = authSocialUser(reqDTO.getSocialType(), reqDTO.getUserType(),
                reqDTO.getCode(), reqDTO.getState());
        Assert.notNull(socialUser, "社交用户不能为空");

        // 社交用户可能之前绑定过别的用户，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndSocialUserId(reqDTO.getUserType(), socialUser.getId());

        // 用户可能之前已经绑定过该社交类型，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(reqDTO.getUserType(), reqDTO.getUserId(),
                socialUser.getType());

        // 绑定当前登录的社交用户
        SocialUserBindDO socialUserBind = SocialUserBindDO.builder()
                .userId(reqDTO.getUserId()).userType(reqDTO.getUserType())
                .socialUserId(socialUser.getId()).socialType(socialUser.getType()).build();
        socialUserBindMapper.insert(socialUserBind);
        return socialUser.getOpenid();
    }

    @Override
    public void unbindSocialUser(Long userId, Integer userType, Integer socialType, String openid) {
        // 获得 openid 对应的 SocialUserDO 社交用户
        SocialUserDO socialUser = selectByTypeAndOpenid(socialType, openid);
        if (socialUser == null) {
            throw exception(SOCIAL_USER_NOT_FOUND);
        }

        // 获得对应的社交绑定关系
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(userType, userId, socialUser.getType());
    }

    @Override
    public SocialUserDO selectByTypeAndOpenid(Integer socialType, String openid) {
        LambdaQueryWrapper<SocialUserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper = wrapper.eq(SocialUserDO::getType, socialType).eq(SocialUserDO::getOpenid, openid);
        List<SocialUserDO> userList = socialUserMapper.selectList(wrapper);
        if (userList!=null && userList.size()>0){
            return userList.get(0);
        }
        return null;
    }

    @Override
    public SocialUserRespDTO getSocialUser(Integer userType, Integer socialType, String code, String state) {
        // 获得社交用户
        SocialUserDO socialUser = authSocialUser(socialType, userType, code, state);
        Assert.notNull(socialUser, "社交用户不能为空");

        // 如果未绑定的社交用户，则无法自动登录，进行报错
        SocialUserBindDO socialUserBind = socialUserBindMapper.selectByUserTypeAndSocialUserId(userType,
                socialUser.getId());
        if (socialUserBind == null) {
            throw exception(AUTH_THIRD_LOGIN_NOT_BIND);
        }
        return new SocialUserRespDTO(socialUser.getOpenid(), socialUserBind.getUserId());
    }

    /**
     * 授权获得对应的社交用户
     * 如果授权失败，则会抛出 {@link ServiceException} 异常
     *
     * @param socialType 社交平台的类型 {@link SocialTypeEnum}
     * @param userType 用户类型
     * @param code     授权码
     * @param state    state
     * @return 授权用户
     */
    @NotNull
    public SocialUserDO authSocialUser(Integer socialType, Integer userType, String code, String state) {
        // 优先从 DB 中获取，因为 code 有且可以使用一次。
        // 在社交登录时，当未绑定 User 时，需要绑定登录，此时需要 code 使用两次
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndCodeAnState(socialType, code, state);
        if (socialUser != null) {
            return socialUser;
        }

        // 请求获取
        AuthUser authUser = socialClientService.getAuthUser(socialType, userType, code, state);
        Assert.notNull(authUser, "三方用户不能为空");

        // 保存到 DB 中
        socialUser = selectByTypeAndOpenid(socialType, authUser.getUuid());
        if (socialUser == null) {
            socialUser = new SocialUserDO();
        }
        socialUser.setType(socialType).setCode(code).setState(state) // 需要保存 code + state 字段，保证后续可查询
                .setOpenid(authUser.getUuid()).setToken(authUser.getToken().getAccessToken()).setRawTokenInfo((toJsonString(authUser.getToken())))
                .setNickname(authUser.getNickname()).setAvatar(authUser.getAvatar()).setRawUserInfo(toJsonString(authUser.getRawUserInfo()));
        if (socialUser.getId() == null) {
            socialUserMapper.insert(socialUser);
        } else {
            socialUserMapper.updateById(socialUser);
        }
        return socialUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SocialUserDO createSocialUser(Long userId, Integer userType, Integer socialType, String openid,String unionid) {
        SocialUserDO socialUser = new SocialUserDO();
        socialUser.setType(socialType);
        socialUser.setOpenid(openid);
        socialUser.setUnionid(unionid);
        socialUserMapper.insert(socialUser);

        // 绑定当前登录的社交用户
        SocialUserBindDO socialUserBind = SocialUserBindDO.builder()
                .userId(userId).userType(userType)
                .socialUserId(socialUser.getId()).socialType(socialUser.getType()).build();
        socialUserBindMapper.insert(socialUserBind);
        return socialUser;
    }

    @Override
    public void update(SocialUserDO data) {
        socialUserMapper.updateById(data);
    }

    @Override
    public Long getUserIdByUnionid(String unionid) {
        Long result = null;
        if (StringUtils.isNotEmpty(unionid)){
            SocialUserDO socialUserDO = socialUserMapper.selectOne(SocialUserDO::getUnionid,unionid);
            if (socialUserDO!=null){
                SocialUserBindDO userBindDO = socialUserBindMapper.selectOne(SocialUserBindDO::getSocialUserId,socialUserDO.getId());
                if (userBindDO!=null){
                    result = userBindDO.getUserId();
                }
            }
        }
        return result;
    }

    @Override
    public String getOpenIdByUserId(Long userId) {
        if (userId!=null){
            LambdaQueryWrapper<SocialUserBindDO> wrapper = new LambdaQueryWrapper<>();
            wrapper = wrapper.eq(SocialUserBindDO::getUserId,userId);
            List<SocialUserBindDO> socialUserBindList = socialUserBindMapper.selectList(wrapper);
            if (socialUserBindList!=null && socialUserBindList.size()>0){
                SocialUserBindDO socialUserBindDO = socialUserBindList.get(0);
                if (socialUserBindDO!=null && socialUserBindDO.getSocialUserId()!=null){
                    SocialUserDO socialUserDO = getSocialUser(socialUserBindDO.getSocialUserId());
                    if (socialUserDO!=null){
                        return socialUserDO.getOpenid();
                    }
                }
            }
        }
        return null;
    }

    // ==================== 社交用户 CRUD ====================

    @Override
    public SocialUserDO getSocialUser(Long id) {
        return socialUserMapper.selectById(id);
    }

    @Override
    public PageResult<SocialUserDO> getSocialUserPage(SocialUserPageReqVO pageReqVO) {
        return socialUserMapper.selectPage(pageReqVO);
    }

}
