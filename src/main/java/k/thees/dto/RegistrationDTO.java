package k.thees.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import k.thees.validation.ValidationConstraints;

import java.util.Objects;

public class RegistrationDTO {

    @NotBlank
    @Size(min = 8, message = "Password must have at least {min} characters")
    public String password;

    @Size(min = ValidationConstraints.USERNAME_MIN_LENGTH, max = ValidationConstraints.USERNAME_MAX_LENGTH,
            message = "Username must be between {min} and {max} characters")
    public String username;

    @Email
    public String email;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDTO that = (RegistrationDTO) o;
        return Objects.equals(password, that.password) && Objects.equals(username, that.username) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username, email);
    }
}
