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

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin/pay").access("hasRole('ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
                .anyRequest().authenticated();

        http.formLogin();

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
