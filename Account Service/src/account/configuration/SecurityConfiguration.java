package account.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfiguration(UserDetailsService userDetailsService, AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests()
                .mvcMatchers("/api/auth/signup", "/actuator/**").permitAll()
                .mvcMatchers(HttpMethod.GET,"/api/security/events").hasRole("AUDITOR")
                .mvcMatchers(HttpMethod.GET,"/api/acct/payments", "/api/empl/payment").hasAnyRole("ACCOUNTANT", "USER")
                .mvcMatchers(HttpMethod.POST,"/api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers(HttpMethod.PUT,"/api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                .antMatchers("/h2-console/**").permitAll().and().headers().frameOptions().sameOrigin()
                .and().authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint::commence)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler::handle);

        return http.build();
    }


}
