package account.repos;

import account.dtos.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User getUserByEmailIgnoreCase(String email);

    boolean existsUsersByEmail(String email);

}
