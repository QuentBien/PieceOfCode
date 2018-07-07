package com.ajd.pieceOfCode.configuration;

import com.ajd.pieceOfCode.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final ObjectMapper objectMapper;
    private static final String JSESSIONID_COOKIE_NAME = "JSESSIONID";
    private static final String HOME = "/";
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";

    @Autowired
    public SecurityConfiguration(UserService userDetailsService, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new TransactionalDaoAuthenticationProvider(userDetailsService);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AppAuthenticationFailureHandler(objectMapper);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().headers().frameOptions().and()
                .and().formLogin().usernameParameter("email")
                .successHandler((request, response, authentication) -> response.setStatus(SC_OK))
                .failureHandler(authenticationFailureHandler())
                .and().sessionManagement().sessionFixation().newSession()
                .and().logout().deleteCookies(JSESSIONID_COOKIE_NAME).invalidateHttpSession(true).logoutSuccessHandler((request, response, authentication) -> response.setStatus(SC_OK))
                .and().exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and().authorizeRequests()
                .antMatchers(GET, HOME).permitAll()
                .antMatchers(POST, LOGIN).permitAll()
                .antMatchers(GET, LOGOUT).authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }
}
