package account.controllers;

import account.dtos.entities.SecurityEvent;
import account.services.SecurityEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityEventController {

    private final SecurityEventService securityEventService;

    public SecurityEventController(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @GetMapping("api/security/events")
    public Iterable<SecurityEvent> getSecurityEvents() {
        return securityEventService.getSecurityEvents();
    }
}
