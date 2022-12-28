package account.dtos;

public class UserSalaryRecordResponse {


    private String name;
    private String lastname;
    private String period;
    private String salary;

    public String getName() {
        return name;
    }

    public UserSalaryRecordResponse setName(String name) {
        this.name = name;

        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserSalaryRecordResponse setLastname(String lastname) {
        this.lastname = lastname;

        return this;
    }

    public String getPeriod() {
        return period;
    }

    public UserSalaryRecordResponse setPeriod(String period) {
        this.period = period;

        return this;
    }

    public String getSalary() {
        return salary;
    }

    public UserSalaryRecordResponse setSalary(String salary) {
        this.salary = salary;

        return this;
    }
}
