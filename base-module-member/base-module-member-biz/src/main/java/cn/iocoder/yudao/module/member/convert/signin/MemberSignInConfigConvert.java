package cn.iocoder.yudao.module.member.convert.signin;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.admin.signin.vo.config.MemberSignInConfigCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.signin.vo.config.MemberSignInConfigRespVO;
import cn.iocoder.yudao.module.member.controller.admin.signin.vo.config.MemberSignInConfigUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.signin.vo.config.AppMemberSignInConfigRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到规则 Convert
 *
 * @author QingX
 */
@Mapper
public interface MemberSignInConfigConvert {

    MemberSignInConfigConvert INSTANCE = Mappers.getMapper(MemberSignInConfigConvert.class);


    default MemberSignInConfigDO convert(MemberSignInConfigCreateReqVO bean){
        MemberSignInConfigDO memberSignInConfigDO = BeanUtils.toBean(bean,MemberSignInConfigDO.class);
        return memberSignInConfigDO;
    };

    default MemberSignInConfigDO convert(MemberSignInConfigUpdateReqVO bean){
        MemberSignInConfigDO memberSignInConfigDO = BeanUtils.toBean(bean,MemberSignInConfigDO.class);
        return memberSignInConfigDO;
    };


    default MemberSignInConfigRespVO convert(MemberSignInConfigDO bean){
        MemberSignInConfigRespVO memberSignInConfigRespVO = BeanUtils.toBean(bean,MemberSignInConfigRespVO.class);
        return memberSignInConfigRespVO;
    };

    default List<MemberSignInConfigRespVO> convertList(List<MemberSignInConfigDO> list){
        if ( list == null ) {
            return null;
        }

        List<MemberSignInConfigRespVO> list1 = new ArrayList<MemberSignInConfigRespVO>( list.size() );
        for ( MemberSignInConfigDO memberSignInConfigDO : list ) {
            list1.add( convert( memberSignInConfigDO ) );
        }

        return list1;
    };

    List<AppMemberSignInConfigRespVO> convertList02(List<MemberSignInConfigDO> list);

    default List<AppMemberSignInConfigRespVO> convertList03(List<MemberSignInConfigDO> list){
        if ( list == null ) {
            return null;
        }

        List<AppMemberSignInConfigRespVO> list1 = new ArrayList<AppMemberSignInConfigRespVO>( list.size() );
        for ( MemberSignInConfigDO memberSignInConfigDO : list ) {
            list1.add( BeanUtils.toBean(memberSignInConfigDO,AppMemberSignInConfigRespVO.class));
        }

        return list1;
    };


}
