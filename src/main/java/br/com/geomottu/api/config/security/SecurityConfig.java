package br.com.geomottu.api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(a -> a
                        // Permite acesso público à página de login e recursos estáticos
                        .requestMatchers("/login", "/registrar", "/acesso-negado", "/css/**", "/js/**", "/images/**").permitAll()
                        // Apenas ADMIN pode acessar as páginas de filiais
                        .requestMatchers("/filiais/**", "/admin/**").hasRole("ADMIN")

                        // Apenas ADMIN pode usar DELETE, PUT, ou PATCH em pátios e motos
                        .requestMatchers(HttpMethod.DELETE, "/patios/**", "/motos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/patios/**", "/motos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/patios/**", "/motos/**").hasRole("ADMIN")

                        // ADMIN e USER podem usar GET e POST
                        .requestMatchers(HttpMethod.GET, "/patios/**", "/motos/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/patios/**", "/motos/**").hasAnyRole("ADMIN", "USER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .exceptionHandling(e -> e
                        .accessDeniedPage("/acesso-negado"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
