package cn.iocoder.yudao.module.member.framework.datapermission;

import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 部门权限控制
 * 公会权限控制
 */
@Configuration(proxyBeanMethods = false)
public class ReservationConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer memberDeptDataPermissionRuleCustomizer(){
        return rule -> {
            // dept
            rule.addDeptColumn(MemberUserDO.class);
        };
    }
}
