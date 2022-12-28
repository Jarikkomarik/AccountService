package account.services;

import account.dtos.Action;
import account.dtos.ChangeRoleDto;
import account.dtos.LockOperation;
import account.dtos.LockUserDto;
import account.dtos.entities.User;
import account.dtos.entities.UserGroup;
import account.repos.UserGroupRepository;
import account.repos.UserRepository;
import account.util.Operation;
import org.springframework.stereotype.Service;

@Service
public class AdminService {


    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    private final AdminValidationService adminValidationService;

    private final SecurityEventService securityEventService;


    public AdminService(UserRepository userRepository, UserGroupRepository userGroupRepository, AdminValidationService adminValidationService, SecurityEventService securityEventService) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.adminValidationService = adminValidationService;
        this.securityEventService = securityEventService;
    }

    public User changeRole(ChangeRoleDto changeRoleDto, String adminEmail) {

        User user = userRepository.getUserByEmailIgnoreCase(changeRoleDto.getUser());

        changeRoleDto.setRole(changeRoleDto.getRole());

        adminValidationService.checkRoleChangeGeneral(user, changeRoleDto.getRole());


        if (changeRoleDto.getOperation() == Operation.GRANT) {
            adminValidationService.checkRoleChangeGrant(user, changeRoleDto.getRole());
            user.getRoles().add(userGroupRepository.findUserGroupByName(changeRoleDto.getRole()));
            securityEventService.registerEvent(Action.GRANT_ROLE, adminEmail, securityEventService.getGrantRoleObjectMessage(changeRoleDto.getRole(), changeRoleDto.getUser()), "/api/admin/user/role");
        } else {
            adminValidationService.checkRoleChangeDeletion(user, changeRoleDto.getRole());
            user.getRoles().remove(new UserGroup(changeRoleDto.getRole()));
            securityEventService.registerEvent(Action.REMOVE_ROLE, adminEmail, securityEventService.getRemoveRoleObjectMessage(changeRoleDto.getRole(), changeRoleDto.getUser()), "/api/admin/user/role");
        }

        userRepository.save(user);
        System.out.println(user);
        return user;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void deleteUser(String email, String adminEmail) {
        User user = userRepository.getUserByEmailIgnoreCase(email);
        adminValidationService.checkUserDeletion(user);
        userRepository.delete(user);
        securityEventService.registerEvent(Action.DELETE_USER, adminEmail, email, "/api/admin/user");
    }

    public void changeLock(LockUserDto lockUserDto, String adminEmail) {
        User user = userRepository.getUserByEmailIgnoreCase(lockUserDto.getEmail());
        adminValidationService.checkUserLock(user, lockUserDto.getLockOperation());

        LockOperation lockOperation = lockUserDto.getLockOperation();
        user.setLocked(lockOperation.getLockStatus());
        securityEventService.registerEvent(lockOperation.getAction(), adminEmail, lockOperation.getEventMsg() + lockUserDto.getEmail().toLowerCase(), "/api/admin/user/access");

        userRepository.save(user);
    }
}
