package account.configuration;

import account.dtos.Action;
import account.services.LoginAttemptService;
import account.services.SecurityEventService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {


    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;
    private final SecurityEventService securityEventService;

    public AuthenticationFailureListener(HttpServletRequest request, LoginAttemptService loginAttemptService, SecurityEventService securityEventService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
        this.securityEventService = securityEventService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        securityEventService.registerEvent(Action.LOGIN_FAILED, event.getAuthentication().getPrincipal().toString(), request.getRequestURI(), request.getRequestURI());

        loginAttemptService.loginFailed(event.getAuthentication().getPrincipal().toString(), request.getRequestURI());

    }
}
