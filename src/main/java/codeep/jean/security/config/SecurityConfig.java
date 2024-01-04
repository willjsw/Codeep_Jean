package codeep.jean.security.config;


import codeep.jean.security.auth.PrincipalDetailsService;
import codeep.jean.security.jwt.JwtFilter;
import codeep.jean.security.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig  {

    private final PrincipalDetailsService principalDetailsService;
    private final RedisUtil redisUtil;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Refresh-Token"));
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final @NotNull HttpSecurity http) throws Exception {
        http.httpBasic(HttpBasicConfigurer::disable)
                .csrf((csrf) -> csrf.disable())
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                                authorize
                                .requestMatchers(publicEndpoints()).permitAll()
                                .requestMatchers(userEndpoints()).access(new WebExpressionAuthorizationManager("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')"))
                                .anyRequest().permitAll())
                .addFilterBefore(new JwtFilter(principalDetailsService, redisUtil), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    private RequestMatcher publicEndpoints() {
        return new OrRequestMatcher(
                //user
                new AntPathRequestMatcher("/user/join","POST"),
                new AntPathRequestMatcher("/user/login2","POST"),
                new AntPathRequestMatcher("/family/join","POST")

        );
    }

    private RequestMatcher userEndpoints() {
        return new OrRequestMatcher(
                //user

                new AntPathRequestMatcher("/user/**","PATCH"), //update
                new AntPathRequestMatcher("/user/logout","POST") //logout
        );

    }


}
