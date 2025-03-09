package cn.iocoder.yudao.module.member.api.feedback;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.api.feedback.dto.MemberFeedbackDto;
import cn.iocoder.yudao.module.member.dal.dataobject.feedback.MemberFeedbackDO;
import cn.iocoder.yudao.module.member.dal.mysql.feedback.MemberFeedbackMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemberFeedbackApilmpl implements MemberFeedbackApi {

    @Resource
    private MemberFeedbackMapper memberFeedbackMapper;

    @Override
    public Integer add(MemberFeedbackDto memberFeedbackDto) {
        return memberFeedbackMapper.insert(BeanUtils.toBean(memberFeedbackDto, MemberFeedbackDO.class));
    }
}
