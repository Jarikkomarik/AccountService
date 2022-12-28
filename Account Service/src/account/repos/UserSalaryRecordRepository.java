package account.repos;

import account.dtos.UserSalaryRecordPKId;
import account.dtos.entities.UserSalaryRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserSalaryRecordRepository extends CrudRepository<UserSalaryRecord, UserSalaryRecordPKId> {
    List<UserSalaryRecord> findAllByEmployee(String employee);
}
