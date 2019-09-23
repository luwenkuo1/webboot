//package com.nokia.web.config;
//
//import org.jasig.cas.client.session.SingleSignOutFilter;
//import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.cas.ServiceProperties;
//import org.springframework.security.cas.authentication.CasAuthenticationProvider;
//import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
//import org.springframework.security.cas.web.CasAuthenticationFilter;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.logout.LogoutFilter;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
//import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import java.util.Collections;
//
///**
// * @author zhg
// * @version $ Id: SecurityConfiguration.java,V 0.1 2019/4/7 22:33 zhg Exp $
// */
//@Profile(value = "prod")
//@Configuration
//@EnableConfigurationProperties(CasProperties.class)
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    private static Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
//
//    private CasProperties casProperties;
//
//    @Value(value = "${app.url}")
//    private String appUrl;
//
//    @Value(value = "${app.loginPath}")
//    private String appLoginPath;
//
//    @Value(value = "${app.logout}")
//    private String appLogout;
//
//    @Value(value = "${app.index}")
//    private String appIndex;
//
//    @Autowired
//    public SecurityConfiguration(CasProperties casProperties) {
//        this.casProperties = casProperties;
//    }
//
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        try {
//            auth.authenticationProvider(casAuthenticationProvider());
//        } catch (Exception e) {
//            logger.error("SSO配置出错", e);
//        }
//    }
//
//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers("/fonts/**").antMatchers("/images/**").antMatchers("/scripts/**")
//                .antMatchers("/styles/**").antMatchers("/views/**").antMatchers("/i18n/**").antMatchers("/xbkj-fast/**").antMatchers(
//                // swagger配置
//                "/swagger-resources/**", "/swagger-ui.html", "/images/**", "/webjars/**", "/v2/api-docs",
//                "/configuration/ui", "/configuration/security"
//        );
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and().headers().frameOptions().disable();
//
//        http.exceptionHandling()
//                .authenticationEntryPoint(casAuthenticationEntryPoint())
//                .and().addFilter(casAuthenticationFilter())
//                .addFilterBefore(casLogoutFilter(), LogoutFilter.class)
//                .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);
//        http.csrf().disable();
//    }
//
//    @Bean
//    public CasAuthenticationFilter casAuthenticationFilter() {
//        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
//        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
//        casAuthenticationFilter.setFilterProcessesUrl(appLoginPath);
//        return casAuthenticationFilter;
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManager() {
//        return authentication -> casAuthenticationProvider().authenticate(authentication);
//    }
//
//    @Bean
//    public CasAuthenticationProvider casAuthenticationProvider() {
//        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
//        casAuthenticationProvider.setUserDetailsService(userDetailsService());
//        casAuthenticationProvider.setServiceProperties(serviceProperties());
//        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
//        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
//        return casAuthenticationProvider;
//    }
//
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        return s -> new User(s,s, Collections.emptyList());
//    }
//
//    @Bean
//    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
//        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
//        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
//        casAuthenticationEntryPoint.setLoginUrl(casProperties.getServerLoginUrl());
//        return casAuthenticationEntryPoint;
//    }
//
//    @Bean
//    public ServiceProperties serviceProperties() {
//        ServiceProperties sp = new ServiceProperties();
//        sp.setService(appUrl + appLoginPath);
//        sp.setSendRenew(false);
//        return sp;
//    }
//
//    @Bean
//    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
//        return new Cas20ServiceTicketValidator(casProperties.getServerUrl());
//    }
//
//
//    @Bean
//    public SingleSignOutFilter singleSignOutFilter() {
//        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
//        singleSignOutFilter.setCasServerUrlPrefix(casProperties.getServerUrl());
//        singleSignOutFilter.setIgnoreInitConfiguration(true);
//        return singleSignOutFilter;
//    }
//
//    @Bean
//    public LogoutFilter casLogoutFilter() {
//        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getServerLogoutUrl(), new SecurityContextLogoutHandler());
//        logoutFilter.setFilterProcessesUrl(appLogout);
//        return logoutFilter;
//    }
//
//    @Bean
//    public SessionAuthenticationStrategy sessionStrategy() {
//        return new SessionFixationProtectionStrategy();
//    }
//
//    @Bean
//    public WebMvcConfigurerAdapter forwardToIndex() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addViewControllers(ViewControllerRegistry registry) {
//                registry.addViewController("/").setViewName(
//                        "forward:" + appIndex);
//            }
//        };
//    }
//}
