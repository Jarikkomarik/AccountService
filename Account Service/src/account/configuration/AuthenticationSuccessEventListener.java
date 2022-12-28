package account.configuration;

import account.services.LoginAttemptService;
import account.services.SecurityEventService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;
    private final SecurityEventService securityEventService;

    public AuthenticationSuccessEventListener(HttpServletRequest request, LoginAttemptService loginAttemptService, SecurityEventService securityEventService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
        this.securityEventService = securityEventService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        loginAttemptService.resetLoginCounter(event.getAuthentication().getName());
    }
}
