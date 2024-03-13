package com.example.demo.config;

import com.example.demo.component.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.demo.entity.user.Permission.*;
import static com.example.demo.entity.user.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
                                // Posts
                                .requestMatchers("/api/posts/**").hasAnyRole(ADMIN.name(), POSTS.name())
                                .requestMatchers(GET, "/api/posts/**").hasAnyAuthority(ADMIN_READ.name(), USER_POSTS_READ.name())
                                .requestMatchers(POST, "/api/posts/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_POSTS_CREATE.name())
                                .requestMatchers(PUT, "/api/posts/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_POSTS_UPDATE.name())
                                .requestMatchers(DELETE, "/api/posts/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_POSTS_DELETE.name())
                                // Users
                                .requestMatchers("/api/users/**").hasAnyRole(ADMIN.name(), USERS.name())
                                .requestMatchers(GET, "/api/users/**").hasAnyAuthority(ADMIN_READ.name(), USER_USERS_READ.name())
                                .requestMatchers(POST, "/api/users/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_USERS_CREATE.name())
                                .requestMatchers(PUT, "/api/users/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_USERS_UPDATE.name())
                                .requestMatchers(DELETE, "/api/users/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_USERS_DELETE.name())
                                // Albums
                                .requestMatchers("/api/albums/**").hasAnyRole(ADMIN.name(), ALBUMS.name())
                                .requestMatchers(GET, "/api/albums/**").hasAnyAuthority(ADMIN_READ.name(), USER_ALBUMS_READ.name())
                                .requestMatchers(POST, "/api/albums/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_ALBUMS_CREATE.name())
                                .requestMatchers(PUT, "/api/albums/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_ALBUMS_UPDATE.name())
                                .requestMatchers(DELETE, "/api/albums/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_ALBUMS_DELETE.name())

                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
