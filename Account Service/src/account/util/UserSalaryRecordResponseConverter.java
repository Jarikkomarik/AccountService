package account.util;

import account.dtos.UserSalaryRecordResponse;
import account.dtos.entities.User;
import account.dtos.entities.UserSalaryRecord;

import java.time.YearMonth;

public class UserSalaryRecordResponseConverter {
    private static final String SALARY_RESPONSE_TEMPLATE = "%s dollar(s) %s cent(s)";
    public static UserSalaryRecordResponse convertUserAndUserRecordToUserSalaryRecordResponse(User user, UserSalaryRecord userSalaryRecord) {
        return new UserSalaryRecordResponse()
                .setName(user.getName())
                .setLastname(user.getLastName())
                .setPeriod(getMonthAndYear(userSalaryRecord.getPeriod()))
                .setSalary(getSalaryInUSDAndCents(userSalaryRecord.getSalary()));
    }

    private static String getMonthAndYear (YearMonth yearMonth) {
        String month = yearMonth.getMonth().toString();
        String year = String.valueOf(yearMonth.getYear());

        return month.charAt(0) + month.substring(1).toLowerCase() + "-" + year;
    }

    private static String getSalaryInUSDAndCents (long salaryInCents){
        String usdValue = String.valueOf(salaryInCents / 100);
        String centsModulo = String.valueOf(salaryInCents % 100);

        return String.format(SALARY_RESPONSE_TEMPLATE, usdValue, centsModulo);
    }
}
