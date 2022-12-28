package account.controllers;

import account.dtos.ChangeRoleDto;
import account.dtos.LockUserDto;
import account.dtos.entities.User;
import account.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;
/*
        PUT api/admin/user/role sets the roles;
        DELETE api/admin/user deletes users;
        GET api/admin/user obtains information about all users; the information should not be sensitive.
        The roles should be divided into 2 groups: administrative (Administrator) and business users (Accountant, User).
        Do not mix up the groups; a user can be either from the administrative or business group. A user with an administrative role should not have access to business functions and vice versa.

*/

@RestController
@Validated
public class AdminController {
    private final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    private final String changeLockResponseTemplate = "User %s %sed!";

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("api/admin/user")
    public Iterable<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("api/admin/user/{email}")
    public Map<String,String> doActionDelete(@PathVariable @NotBlank String email, @AuthenticationPrincipal UserDetails details) {
        adminService.deleteUser(email, details.getUsername());
        return Map.of("user", email, "status", "Deleted successfully!");
    }

    @PutMapping("api/admin/user/role")
    public User changeRole(@RequestBody @Valid ChangeRoleDto changeRoleDto, @AuthenticationPrincipal UserDetails details) {
        return adminService.changeRole(changeRoleDto, details.getUsername());
    }

    @PutMapping("api/admin/user/access")
    public Map<String,String> changeLock(@RequestBody LockUserDto lockUserDto, @AuthenticationPrincipal UserDetails details) {
        adminService.changeLock(lockUserDto,  details.getUsername());
        return Map.of("status", String.format(changeLockResponseTemplate, lockUserDto.getEmail().toLowerCase(), lockUserDto.getLockOperation().toString().toLowerCase()));
    }
}
