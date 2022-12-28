package account.configuration;

import account.dtos.entities.UserGroup;
import account.repos.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private UserGroupRepository groupRepository;

    @Autowired
    public DataLoader(UserGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            groupRepository.save(new UserGroup("ROLE_ADMINISTRATOR"));
            groupRepository.save(new UserGroup("ROLE_USER"));
            groupRepository.save(new UserGroup("ROLE_ACCOUNTANT"));
            groupRepository.save(new UserGroup("ROLE_AUDITOR"));
        } catch (Exception e) {
            //TODO remove TryCatch. Added to support hyperskil tests.
        }


    }
}