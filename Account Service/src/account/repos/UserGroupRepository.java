package account.repos;

import account.dtos.entities.UserGroup;
import org.springframework.data.repository.CrudRepository;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {
    UserGroup findUserGroupByName(String name);
}
