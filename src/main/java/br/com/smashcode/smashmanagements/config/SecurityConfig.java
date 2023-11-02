package br.com.smashcode.smashmanagements.config;

import br.com.smashcode.smashmanagements.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/checkpoint", true)
                        .permitAll()
                )
                .logout(logout -> {
                    logout.logoutUrl("/logout").logoutSuccessUrl("/login");
                })
                .addFilterBefore(new LoginFilter(userRepository), OAuth2LoginAuthenticationFilter.class)
                .build();
    }
}
