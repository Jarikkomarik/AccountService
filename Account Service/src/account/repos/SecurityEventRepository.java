package account.repos;

import account.dtos.entities.SecurityEvent;
import org.springframework.data.repository.CrudRepository;

public interface SecurityEventRepository extends CrudRepository<SecurityEvent, Long> {

}
