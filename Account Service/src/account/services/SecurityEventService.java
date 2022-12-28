package account.services;

import account.dtos.Action;
import account.dtos.entities.SecurityEvent;
import account.dtos.entities.User;
import account.dtos.entities.UserGroup;
import account.repos.SecurityEventRepository;
import account.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class SecurityEventService {
    private final SecurityEventRepository securityEventRepository;
    private final UserRepository userRepository;



    private final String GRANT_ROLE_OBJECT_TEMPLATE = "Grant role %s to %s";
    private final String REMOVE_ROLE_OBJECT_TEMPLATE = "Remove role %s from %s";

    public SecurityEventService(SecurityEventRepository securityEventRepository, UserRepository userRepository) {
        this.securityEventRepository = securityEventRepository;
        this.userRepository = userRepository;
    }

    public void registerEvent(Action action, String subject, String object, String path) {

        securityEventRepository.save(new SecurityEvent()
                .setDate(LocalDate.now())
                .setAction(action)
                .setSubject(subject)
                .setObject(object)
                .setPath(path)
        );
    }

    public String getGrantRoleObjectMessage(String role, String email) {
        return String.format(GRANT_ROLE_OBJECT_TEMPLATE, role.split("_")[1], email.toLowerCase());
    }

    public String getRemoveRoleObjectMessage(String role, String email) {
        return String.format(REMOVE_ROLE_OBJECT_TEMPLATE, role.split("_")[1], email.toLowerCase());
    }

    public Iterable<SecurityEvent> getSecurityEvents() {
            return securityEventRepository.findAll();
    }
}
