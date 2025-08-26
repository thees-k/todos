package k.thees.dto;


import java.util.Objects;

public class UpdateUserDTO {
    public String username;
    public String password;
    public String email;
    public Integer roleId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserDTO that = (UpdateUserDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(email, that.email) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, roleId);
    }
}