package account.configuration;

import account.dtos.Action;
import account.services.SecurityEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@org.springframework.context.annotation.Configuration
public class Configuration {
    SecurityEventService securityEventService;

    public Configuration(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @Bean
    public Set<String> breachedPasswords() {
        return Set.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> responseMSG = new LinkedHashMap<>();
            responseMSG.put("timestamp", Instant.now().toString());
            responseMSG.put("status", HttpStatus.FORBIDDEN.value());
            responseMSG.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
            responseMSG.put("message","Access Denied!");
            responseMSG.put("path", request.getRequestURI());

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(mapper.writeValueAsString(responseMSG));

            securityEventService.registerEvent(Action.ACCESS_DENIED, SecurityContextHolder.getContext().getAuthentication().getName(), request.getRequestURI(), request.getRequestURI());
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationFailureHandler() {
        return (request, response, authException) -> {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> responseMSG = new LinkedHashMap<>();
            responseMSG.put("timestamp", Instant.now().toString());
            responseMSG.put("status", HttpStatus.UNAUTHORIZED.value());
            responseMSG.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            responseMSG.put("message","User account is locked");
            responseMSG.put("path", request.getRequestURI());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(mapper.writeValueAsString(responseMSG));
        };
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
