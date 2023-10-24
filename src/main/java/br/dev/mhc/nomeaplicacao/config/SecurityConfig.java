package br.dev.mhc.nomeaplicacao.config;

import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import br.dev.mhc.nomeaplicacao.security.JWTAuthenticationFilter;
import br.dev.mhc.nomeaplicacao.security.JWTAuthorizationFilter;
import br.dev.mhc.nomeaplicacao.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Environment environment;
    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final CustomAuthorizationManager customAuthorizationManager;

    private final String NUMBER_REGEX = "/(\\d+)";
    private final String ANY_CHILD_REGEX = "/(.*)";
    private final String ANY_PATH_REGEX = "?(/.*)?";

    @Autowired
    public SecurityConfig(Environment environment, UserDetailsService userDetailsService, JWTUtil jwtUtil, CustomAuthorizationManager customAuthorizationManager) {
        this.environment = environment;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.customAuthorizationManager = customAuthorizationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        }

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/users")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/refresh-token")).permitAll()
                        .requestMatchers(buildRequestMatcher(HttpMethod.GET, "/users", NUMBER_REGEX)).access(customAuthorizationManager)
                        .requestMatchers(buildRequestMatcher(HttpMethod.PUT, "/users", NUMBER_REGEX)).access(customAuthorizationManager)
                        .anyRequest().hasAnyRole(ProfileEnum.ADMIN.getDescription())
                )
                .addFilter(new JWTAuthenticationFilter(authenticationManager(http), jwtUtil))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(http), jwtUtil, userDetailsService))
                .authenticationManager(authenticationManager(http));
        return http.build();
    }

    public RequestMatcher buildRequestMatcher(HttpMethod httpMethod, String... patterns) {
        String pattern = Stream.of(patterns).reduce("", (a, b) -> a.concat(b));
        return RegexRequestMatcher.regexMatcher(httpMethod, pattern);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

}