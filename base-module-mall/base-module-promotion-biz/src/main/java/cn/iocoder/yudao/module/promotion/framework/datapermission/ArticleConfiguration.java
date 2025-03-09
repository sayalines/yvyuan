package cn.iocoder.yudao.module.promotion.framework.datapermission;


import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import cn.iocoder.yudao.module.promotion.dal.dataobject.article.ArticleDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 部门权限控制
 * 公会权限控制
 */
@Configuration(proxyBeanMethods = false)
public class ArticleConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer articleDeptDataPermissionRuleCustomizer(){
        return rule -> {
            // dept
            rule.addDeptColumn(ArticleDO.class);
            // user
            rule.addUserColumn(ArticleDO.class, "creator");
        };
    }
}
