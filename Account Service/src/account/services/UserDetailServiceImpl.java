package account.services;

import account.dtos.entities.UserGroup;
import account.repos.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SecurityEventService securityEventService;

    public UserDetailServiceImpl(UserRepository userRepository, SecurityEventService securityEventService) {
        this.userRepository = userRepository;
        this.securityEventService = securityEventService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        account.dtos.entities.User user = userRepository.getUserByEmailIgnoreCase(username);
        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(user.getEmail().toLowerCase())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(UserGroup::getName).toArray(String[]::new))
                .accountLocked(user.isLocked())
                .build();
    }
}
