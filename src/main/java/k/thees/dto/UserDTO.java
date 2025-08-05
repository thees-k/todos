package k.thees.dto;

import jakarta.validation.constraints.*;
import k.thees.validation.ValidationConstraints;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO {
    public Long id;

    @Size(min = ValidationConstraints.USERNAME_MIN_LENGTH, max = ValidationConstraints.USERNAME_MAX_LENGTH,
            message = "Username must be between {min} and {max} characters")
    public String username;

    @Email(message = "Email should be valid")
    public String email;

    public String passwordHash;
    public LocalDateTime updatedAt;

    public UserDTO() {}

    public UserDTO(Long id, String username, String email, String passwordHash, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username) && Objects.equals(email, userDTO.email) && Objects.equals(passwordHash, userDTO.passwordHash) && Objects.equals(updatedAt, userDTO.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, passwordHash, updatedAt);
    }
}