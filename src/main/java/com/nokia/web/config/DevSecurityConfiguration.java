package com.nokia.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @author zhg
 * @version $ Id: WebConfigurer.java,V 0.1 2019/4/8 21:27 zhg Exp $
 */
@Profile(value = "dev")
@Configuration
@EnableWebSecurity
public class DevSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(DevSecurityConfiguration.class);

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/fonts/**").antMatchers("/**/**").antMatchers("/scripts/**")
                .antMatchers("/styles/**").antMatchers("/view/**").antMatchers("/views/**").antMatchers("/i18n/**").antMatchers("/swagger-resources/**").antMatchers("/swagger-ui.html").antMatchers("/v2/api-docs").antMatchers("/webjars/**").antMatchers("/xbkj-fast/**").antMatchers(
                // swagger配置
                "/swagger-resources/**", "/swagger-ui.html", "/images/**", "/webjars/**", "/v2/api-docs",
                "/configuration/ui", "/configuration/security"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll()
                .and().headers().frameOptions().disable();
        http.csrf().disable();
    }

    @Bean
    public SessionAuthenticationStrategy sessionStrategy() {
        return new SessionFixationProtectionStrategy();
    }

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName(
                        "forward:/gis/ltecell.html");
                registry.addRedirectViewController("/api/customer-api/api-docs",
                        "/api/api-docs").setKeepQueryParams(true);
                registry.addRedirectViewController("/api/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/ui");
                registry.addRedirectViewController("/api/swagger-resources/configuration/security",
                        "/swagger-resources/configuration/security");
                registry.addRedirectViewController("/api/swagger-resources", "/swagger-resources");
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/api/**").addResourceLocations("classpath:/META-INF/resources/");
            }
        };

    }
}