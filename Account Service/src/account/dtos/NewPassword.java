package account.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;

public class NewPassword {

    @JsonProperty(value = "new_password")
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
