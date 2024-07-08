package org.example.itech_heaven.Config;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Permission;
import org.example.itech_heaven.Repository.PermissionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final PersistentTokenRepository persistentTokenRepository;
    private final SecurityHandler securityHandler;
    private final PermissionRepository permissionRepository;
    private String[] urlStatic = {"/webjars/**", "/css/**", "/js/**", "/fonts/**", "/images/**", "/assets/**"};
    private String[] urlAuthentication = {"/login","/error/**","/login-oauth2","/register","/forgot-password", "/reset-password", "/header"};
    private String[] urlGuest = {"/home", "/product", "mail-success", "/contact", "/product-sale", "/product-details", "/deviceDetail", "/devices", "/sale-accesory"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                            List<Permission> permissions = permissionRepository.findAll();
                            permissions.forEach(permission -> authorize.requestMatchers(permission.getUrl()).hasAuthority(permission.getName()));
                            authorize
                                    .requestMatchers(urlStatic).permitAll()
                                    .requestMatchers(urlAuthentication).permitAll()
                                    .requestMatchers(urlGuest).permitAll()
                                    .anyRequest().authenticated();
                        }
                ).formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(securityHandler)
                        .failureUrl("/login?faillg")
                        .permitAll()
                ).logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider)
                .rememberMe(
                        remember -> remember
                                .tokenRepository(persistentTokenRepository)
                                .tokenValiditySeconds(3600)
                )
                .oauth2Login(
                        oauth2 -> oauth2
                                .loginPage("/login")
                                .defaultSuccessUrl("/login-oauth2")
                                .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                )
        ;


        return httpSecurity.build();
    }


}
