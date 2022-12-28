package account.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;

public class UserSalaryRecordPKId implements Serializable {

    private String employee;
    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth period;

    public UserSalaryRecordPKId(String employee, YearMonth period) {
        this.employee = employee;
        this.period = period;
    }

    public UserSalaryRecordPKId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSalaryRecordPKId that = (UserSalaryRecordPKId) o;
        return employee.equals(that.employee) && period.equals(that.period);
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, period);
    }

    @Override
    public String toString() {
        return "UserSalaryRecordPKId{" +
                "employee='" + employee + '\'' +
                ", period=" + period +
                '}';
    }
}
