package account.controllers;

import account.dtos.NewPassword;
import account.dtos.entities.User;
import account.services.SecurityEventService;
import account.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final SecurityEventService securityEventService;


    public AuthController(UserService userDetailService, SecurityEventService securityEventService) {
        this.userService = userDetailService;
        this.securityEventService = securityEventService;
    }

    @PostMapping(value = "/signup")
    public User singUp(@RequestBody @Valid User user) {
        userService.saveUser(user);
        return user;
    }

    @PostMapping(value = "/changepass")
    public Map<String, String> changePass(@RequestBody @Valid NewPassword newPassword, Principal principal) {
        return Map.of("email", userService.changePass(principal.getName(), newPassword.getPassword()), "status", "The password has been updated successfully");
    }


}