package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll() //이거 없으면 안됨
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin/pay").access("hasRole('ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
                .anyRequest().authenticated();

        http.formLogin()
                        .successHandler((request, response, authentication) -> {
                            RequestCache requestCache = new HttpSessionRequestCache();
                            SavedRequest savedReqeust = requestCache.getRequest(request, response);
                            String redirectUrl = savedReqeust.getRedirectUrl();
                            response.sendRedirect(redirectUrl);
                        });

        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login");
                })
                //테스트 하려면 authenticationEntryPoint 람다 함수 주석처리해야함
                //redirect에서 우리가 만든 컨트롤러의 login페이지로 이동하기 때문에 로그인을 할 수가 없음
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/denied");
                });




        http.userDetailsService(userDetailsService());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user1 = User.withUsername("user").password("{noop}1111").roles("USER").build();
        UserDetails user2 = User.withUsername("sys").password("{noop}1111").roles("SYS").build();
        UserDetails user3 = User.withUsername("admin").password("{noop}1111").roles("ADMIN").build();

        return new InMemoryUserDetailsManager(Arrays.asList(user1,user2,user3));
    }
}
