package k.thees.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO {
    public Long id;
    public String username;
    public String email;
    public Integer roleId;
    public String passwordHash;
    public LocalDateTime createdAt;
    public Long updatedById;
    public LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username) && Objects.equals(email, userDTO.email) && Objects.equals(passwordHash, userDTO.passwordHash) && Objects.equals(createdAt, userDTO.createdAt) && Objects.equals(updatedById, userDTO.updatedById) && Objects.equals(updatedAt, userDTO.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, passwordHash, createdAt, updatedById, updatedAt);
    }
}