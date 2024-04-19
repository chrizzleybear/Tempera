package at.qe.skeleton.configs;

import javax.sql.DataSource;

import at.qe.skeleton.jwt.AuthEntryPointJwt;
import at.qe.skeleton.jwt.AuthTokenFilter;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring configuration for web security.
 *
 * <p>This class is part of the skeleton project provided for students of the course "Software
 * Engineering" offered by Innsbruck University.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String ADMIN = UserxRole.ADMIN.name();
    private static final String MANAGER = UserxRole.MANAGER.name();
    private static final String EMPLOYEE = UserxRole.EMPLOYEE.name();
    private static final String LOGIN = "/login.xhtml";
    private static final String ACCESSDENIED = "/error/access_denied.xhtml";

    @Autowired DataSource dataSource;

    @Autowired UserxService userxService;

    @Autowired private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userxService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        try {

            http.csrf(csrf -> csrf.disable())
                    .headers(
                            headers ->
                                    headers.frameOptions(FrameOptionsConfig::sameOrigin)) // needed for H2 console
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(new AntPathRequestMatcher("/rasp/**"))
                            .authenticated()).httpBasic(Customizer.withDefaults())
                    .authorizeHttpRequests(
                            authorize ->
                                    authorize
                                            .requestMatchers(new AntPathRequestMatcher("/api/auth/**"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/api/user/**"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/api/users/**"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/**.jsf"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/jakarta.faces.resource/**"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/error/**"))
                                            .permitAll()
                                            .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                                            .hasAnyAuthority("ADMIN")
                                            .requestMatchers(new AntPathRequestMatcher("/secured/**"))
                                            .hasAnyAuthority(ADMIN, MANAGER, EMPLOYEE)
                                            .anyRequest()
                                            .authenticated())
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(
                            authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                    // :TODO: user failureUrl(/login.xhtml?error) and make sure that a corresponding message
                    // is displayed
                    .sessionManagement(
                            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));

            return http.build();
        } catch (Exception ex) {
            throw new BeanCreationException("Wrong spring security configuration", ex);
        }
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
