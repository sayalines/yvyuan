package cn.iocoder.yudao.server.controller.interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestConfig  implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRestInterceptor())
                .addPathPatterns("/rest/api/**") // 拦截所有路径
                .excludePathPatterns("/rest/api/pay/notify/**","/rest/api/box/queue-info"); // 排除错误页面和静态资源
    }

    @Bean
    public RestInterceptor getRestInterceptor(){
        return new RestInterceptor();
    }
}
