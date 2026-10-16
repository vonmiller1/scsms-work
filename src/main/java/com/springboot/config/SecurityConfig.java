package com.springboot.config;

import com.springboot.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inline filter that checks session and sets authentication
    public class SessionAuthFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            HttpSession session = request.getSession(false);

            if (session != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = (User) session.getAttribute("user");

                if (user != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // Enables CORS config from WebMvcConfigurer
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/css/**", "/js/**").permitAll() //Allow login/signup routes
                        .requestMatchers("/api/bmc/login", "/api/bmc/signup").permitAll() // Allow BMC login/signup
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() // CORS preflight
                        .requestMatchers("/api/**").authenticated() // allow API for logged-in users
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()) // Disable Spring's default form login
                .httpBasic(basic -> basic.disable()) // Disable HTTP basic auth
                .logout(logout -> logout.disable()); // Disable Spring logout (Optional)

        // Add the session filter BEFORE UsernamePasswordAuthenticationFilter
        http.addFilterBefore(new SessionAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
