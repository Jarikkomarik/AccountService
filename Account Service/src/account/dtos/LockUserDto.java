package account.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LockUserDto {
    @JsonProperty("user")
    String email;
    @JsonProperty("operation")
    LockOperation lockOperation;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LockOperation getLockOperation() {
        return lockOperation;
    }

    public void setLockOperation(LockOperation lockOperation) {
        this.lockOperation = lockOperation;
    }
}
