package cn.hisouten.mall.web.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {

            // 全局登录校验：放行登录、注册、文档、错误页
            SaRouter.match("/**")
                    .notMatch("/login", "/register",
                            "/user/login", "/merchant/login", "/admin/login",
                            "/user/register", "/merchant/register",
                            "/error", "/doc.html", "/webjars/**")
                    .notMatch("/user/product/**", "/user/category/**")
                    .check(r -> StpUtil.checkLogin());

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
