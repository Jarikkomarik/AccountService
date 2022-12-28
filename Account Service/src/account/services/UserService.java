package account.services;

import account.dtos.Action;
import account.dtos.entities.User;
import account.dtos.entities.UserGroup;
import account.repos.UserGroupRepository;
import account.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.TreeSet;

@Service
public class UserService {
    private boolean adminRoleIsNotGiven = true;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final SecurityEventService securityEventService;
    private final PasswordEncoder passwordEncoder;
    public Set<String> breachedPasswords;

    public UserService(UserRepository userRepository, UserGroupRepository userGroupRepository, SecurityEventService securityEventService, PasswordEncoder passwordEncoder, Set<String> breachedPasswords) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.securityEventService = securityEventService;
        this.passwordEncoder = passwordEncoder;
        this.breachedPasswords = breachedPasswords;
    }


    public void saveUser(User user) {

        checkIfUserIsAlreadyRegistered(user);
        checkIfUserPasswordIsBreached(user.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEmail(user.getEmail().toLowerCase());

        UserGroup userGroup;

        if(adminRoleIsNotGiven && userRepository.count() == 0) {
            userGroup = userGroupRepository.findUserGroupByName("ROLE_ADMINISTRATOR");
            adminRoleIsNotGiven = false;
        } else {
            userGroup = userGroupRepository.findUserGroupByName("ROLE_USER");
        }
        TreeSet <UserGroup> treeSet = new TreeSet();
        treeSet.add(userGroup);
        user.setRoles(treeSet);

        userRepository.save(user);
        securityEventService.registerEvent(Action.CREATE_USER, "Anonymous", user.getEmail(), "api/auth/signup");
    }

    public String changePass(String userEmail, String newPass) {
        User registeredUser = userRepository.getUserByEmailIgnoreCase(userEmail);

        if(passwordEncoder.matches(newPass, registeredUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        checkIfUserPasswordIsBreached(newPass);

        registeredUser.setPassword(passwordEncoder.encode(newPass));

        userRepository.save(registeredUser);
        securityEventService.registerEvent(Action.CHANGE_PASSWORD, userEmail,userEmail, "api/auth/changepass");
        return registeredUser.getEmail();
    }


    private void checkIfUserIsAlreadyRegistered(User user) {
        if(userRepository.getUserByEmailIgnoreCase(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
    }

    private void checkIfUserPasswordIsBreached(String password) {
        if(breachedPasswords.contains(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");

        }
    }

}
