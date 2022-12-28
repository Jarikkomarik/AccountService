package account.dtos;

import account.util.Operation;

import javax.validation.constraints.NotBlank;

public class ChangeRoleDto {
    @NotBlank
    String user;
    @NotBlank
    String role;
    Operation operation;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if(role.startsWith("ROLE_")){
            this.role = role;
        } else {
            this.role = "ROLE_" + role;
        }

    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "ChangeRoleDto{" +
                "user='" + user + '\'' +
                ", userGroup=" + role +
                ", operation=" + operation +
                '}';
    }
}
