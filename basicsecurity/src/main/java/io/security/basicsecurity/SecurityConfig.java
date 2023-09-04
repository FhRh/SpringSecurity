package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;

@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/login**").permitAll()
                .anyRequest().authenticated();

        http.sessionManagement()
//                .invalidSessionUrl("/invaild")
                //.expireUrl("/expired")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true) // 동시접속 차단
                .and()
                .sessionFixation().changeSessionId()

                //SessionManagementConfigurer.init()
                //시큐리티가 세션을 생성하지 않고 사용하지 않는 것
                //CsrfFilter, HttpSessionCsrfTokenRepository
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
        ;

        return http.build();
    }
}
