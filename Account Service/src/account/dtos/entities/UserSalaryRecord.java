package account.dtos.entities;

import account.dtos.UserSalaryRecordPKId;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.Min;
import java.time.YearMonth;
import java.util.Objects;

@Entity
@IdClass(UserSalaryRecordPKId.class)
public class UserSalaryRecord {
    @Id
    private String employee;
    @Id
    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth period;
    @Min(value = 0)
    private long salary;

    public UserSalaryRecord(String employee, YearMonth period, long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public UserSalaryRecord() {
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public String getEmployee() {
        return employee;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public long getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "UserSalaryRecord{" +
                "employee='" + employee + '\'' +
                ", period=" + period +
                ", salary=" + salary +
                '}';
    }


    @Override
    public int hashCode() {
        return Objects.hash(employee, period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSalaryRecord that = (UserSalaryRecord) o;
        return salary == that.salary && employee.equals(that.employee) && period.equals(that.period);
    }
}
