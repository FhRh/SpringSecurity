package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

        http.logout()
//                .logoutUrl("/logoutexe")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    session.invalidate();
                    SecurityContext context = SecurityContextHolder.getContext();
                    SecurityContextHolder.clearContext();
                    context.setAuthentication(null);
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    System.out.println("logout is succeed");
                    response.sendRedirect("/login");
                });

        return http.build();
    }
}
