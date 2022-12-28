package account.services;

import account.dtos.Action;
import account.dtos.entities.User;
import account.dtos.entities.UserGroup;
import account.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {
    private final SecurityEventService securityEventService;
    private final UserRepository userRepository;
    private final int attemptsLimit = 5;
    private final Map<String, Integer> attemptMap = new HashMap<>();

    public LoginAttemptService(SecurityEventService securityEventService, UserRepository userRepository) {
        this.securityEventService = securityEventService;
        this.userRepository = userRepository;
    }

    public void loginFailed(String email, String requestURI) {

        updateFailedAttempts(email);

        if (attemptLimitIsBreached(email)) {
            securityEventService.registerEvent(Action.BRUTE_FORCE,email, requestURI, requestURI);
            lockUser(email, requestURI);
            attemptMap.remove(email);
        }
    }

    private void lockUser(String email, String requestURI) {
        securityEventService.registerEvent(Action.LOCK_USER, email, "Lock user " + email, requestURI);
        User user = userRepository.getUserByEmailIgnoreCase(email);
        if(user.getRoles().contains(new UserGroup("ROLE_ADMINISTRATOR"))) return;
        user.setLocked(true);
        userRepository.save(user);
    }

    private boolean attemptLimitIsBreached(String email) {
        return attemptMap.get(email) >= attemptsLimit;
    }

    private void updateFailedAttempts(String email) {
        if (attemptMap.containsKey(email)) {
            int attemptCount = attemptMap.get(email);
            attemptCount++;
            attemptMap.put(email, attemptCount);
        } else {
            attemptMap.put(email, 1);
        }
    }

    public void resetLoginCounter(String email) {
        attemptMap.remove(email);
    }
}
