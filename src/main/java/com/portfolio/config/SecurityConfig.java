package com.portfolio.config;

import com.portfolio.data.vo.RoleVO;
import com.portfolio.security.jwt.JwtAuthFilter;
import com.portfolio.security.jwt.JwtService;
import com.portfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/portfolio/{id}")
                    .authenticated()
                .antMatchers(HttpMethod.GET,"/portfolio/**")
                    .hasRole(RoleVO.ADMINISTRATOR.toString())
                .antMatchers(HttpMethod.POST,"/portfolio/**")
                    .authenticated()
                .antMatchers(HttpMethod.PUT,"/portfolio/**")
                    .authenticated()
                .antMatchers(HttpMethod.DELETE,"/portfolio/**")
                    .authenticated()
                .antMatchers(HttpMethod.GET,"/asset/reload")
                    .permitAll()
                .antMatchers(HttpMethod.GET,"/asset/**")
                    .permitAll()
                .antMatchers(HttpMethod.POST,"/asset/**")
                    .hasRole(RoleVO.ADMINISTRATOR.toString())
                .antMatchers(HttpMethod.PUT,"/asset/**")
                    .hasRole(RoleVO.ADMINISTRATOR.toString())
                .antMatchers(HttpMethod.DELETE,"/asset/**")
                    .hasRole(RoleVO.ADMINISTRATOR.toString())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**");
    }
}
