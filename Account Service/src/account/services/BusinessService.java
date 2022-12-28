package account.services;

import account.dtos.UserSalaryRecordPKId;
import account.dtos.UserSalaryRecordResponse;
import account.dtos.entities.User;
import account.dtos.entities.UserSalaryRecord;
import account.repos.UserRepository;
import account.repos.UserSalaryRecordRepository;
import account.util.UserSalaryRecordResponseConverter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    private final UserRepository userRepository;
    private final UserSalaryRecordRepository userSalaryRecordRepository;

    public BusinessService(UserRepository userRepository, UserSalaryRecordRepository userSalaryRecordRepository) {
        this.userRepository = userRepository;
        this.userSalaryRecordRepository = userSalaryRecordRepository;
    }

    public UserSalaryRecordResponse getRecord(String userEmail, YearMonth period) {
        UserSalaryRecord userSalaryRecord = userSalaryRecordRepository.findById(new UserSalaryRecordPKId(userEmail, period)).orElseThrow();
        User user = userRepository.getUserByEmailIgnoreCase(userEmail);

        return UserSalaryRecordResponseConverter.convertUserAndUserRecordToUserSalaryRecordResponse(user, userSalaryRecord);
    }

    public List<UserSalaryRecordResponse> getRecords(String userEmail) {
        List<UserSalaryRecord> UserSalaryRecordList = userSalaryRecordRepository.findAllByEmployee(userEmail);
        User user = userRepository.getUserByEmailIgnoreCase(userEmail);

        return UserSalaryRecordList.stream()
                .sorted((r1, r2) -> {
                    if(r1.getPeriod().equals(r2.getPeriod())){
                        return 0;
                    } else if (r1.getPeriod().isBefore(r2.getPeriod())){
                        return 1;
                    } else {
                        return  -1;
                    }
                })
                .map(userSalaryRecord -> UserSalaryRecordResponseConverter.convertUserAndUserRecordToUserSalaryRecordResponse(user, userSalaryRecord))
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveRecords(List<UserSalaryRecord> userSalaryRecordsList) {

        long registeredUsersCount = userSalaryRecordsList.stream()
                .filter(userSalaryRecord -> userRepository.existsUsersByEmail(userSalaryRecord.getEmployee()))
                .count();

        checkForPeriodDuplicates(userSalaryRecordsList);

        if (registeredUsersCount == userSalaryRecordsList.size()) {
            userSalaryRecordRepository.saveAll(userSalaryRecordsList);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users contains unregistered user.");
        }

    }

    @Transactional
    public void updateRecord(UserSalaryRecord userSalaryRecord) {

        if (userSalaryRecordRepository.existsById(new UserSalaryRecordPKId(userSalaryRecord.getEmployee(), userSalaryRecord.getPeriod()))) {
            userSalaryRecordRepository.save(userSalaryRecord);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record is not present");
        }

    }

    private void checkForPeriodDuplicates(List<UserSalaryRecord> userSalaryRecords) {
        boolean containsDuplicates = userSalaryRecords.stream()
                .map(userSalaryRecord -> new UserSalaryRecordPKId(userSalaryRecord.getEmployee(), userSalaryRecord.getPeriod()))
                .distinct()
                .count() != userSalaryRecords.size();
        if(containsDuplicates) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request contains duplicated periods user.");
        }
    }

}
