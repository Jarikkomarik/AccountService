package account.services;

import account.dtos.LockOperation;
import account.dtos.entities.User;
import account.dtos.entities.UserGroup;
import account.repos.UserGroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class AdminValidationService {

    private final UserGroupRepository userGroupRepository;

    public AdminValidationService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public void checkRoleChangeGeneral(User user, String roleToAdd) {

        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");

        //check if role is found          //ErMsg:"Role not found!"
        if(userGroupRepository.findUserGroupByName(roleToAdd) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");


    }

    public void checkRoleChangeGrant(User user, String roleToAdd) {
        //check if user is granted a business role or vice versa,    //ErMsg:"The user cannot combine administrative and business roles!"
        Set<UserGroup> userGroupSet = user.getRoles();

        if(userGroupSet.contains(new UserGroup("ROLE_ADMINISTRATOR")) || roleToAdd.equals("ROLE_ADMINISTRATOR")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
    }

    public void checkRoleChangeDeletion(User user, String roleToAdd) {

        //DELETE

        //check if user have a role that is requested for deletion   //ErMsg:"The user does not have a role!"
        if(!user.getRoles().contains(new UserGroup(roleToAdd))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The user does not have a role!");


        //check if admin role is being requested for deletion        //ErMsg:"Can't remove ADMINISTRATOR role!"
        if(roleToAdd.equals("ROLE_ADMINISTRATOR")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Can't remove ADMINISTRATOR role!");


        //check if user will have any role after deletion            //ErMsg:"The user must have at least one role!"
        if(user.getRoles().size() < 2) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The user must have at least one role!");

    }

    public void checkUserDeletion(User user) {
        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!" );
        if(user.getRoles().contains(new UserGroup("ROLE_ADMINISTRATOR"))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Can't remove ADMINISTRATOR role!");
    }

    public void checkUserLock(User user, LockOperation lockOperation) {
        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!" );
        if(user.getRoles().contains(new UserGroup("ROLE_ADMINISTRATOR"))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Can't lock the ADMINISTRATOR!");
        if(lockOperation.equals(LockOperation.LOCK) && user.isLocked()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User is already locked");
        if(lockOperation.equals(LockOperation.UNLOCK) && !user.isLocked()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User is already unlocked");
    }

}
