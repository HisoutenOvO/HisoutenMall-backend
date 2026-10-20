package cn.hisouten.mall.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @PostConstruct
    public void init() {
        System.out.println("========= SaTokenConfig 初始化完成 =========");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {

            // 全局登录校验：放行登录、注册、文档、错误页
            SaRouter.match("/**")
                    .notMatch("/error", "/favicon.ico")
                    .notMatch("/doc.html", "/webjars/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**")
                    .notMatch("/user/product/**", "/user/category/**")
                    .notMatch("/admin/login", "/merchant/login", "/merchant/register", "/user/login", "/user/register")
                    .check(r -> {System.out.println("========= 拦截器正在校验路径: " + SaHolder.getRequest().getRequestPath()); StpUtil.checkLogin();});

            // 角色路由：排除各自登录注册接口
            SaRouter.match("/admin/**")
                    .notMatch("/admin/login")
                    .check(r -> StpUtil.checkRole("admin"));

            SaRouter.match("/merchant/**")
                    .notMatch("/merchant/login", "/merchant/register")
                    .check(r -> StpUtil.checkRole("merchant"));

        })).addPathPatterns("/**");
    }
}
