package k.thees.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import k.thees.validation.ValidationConstraints;

import java.util.Objects;

public class RegistrationDTO {

    @NotBlank(message = "Password must not be blank")
    @Size(min = ValidationConstraints.PASSWORD_MIN_LENGTH, max = ValidationConstraints.PASSWORD_MAX_LENGTH,
            message = "Password must be between {min} and {max} characters")
    @Pattern(
            regexp = "^[\\x21-\\x7E]+$",
            message = "Password must not contain whitespace or control characters"
    )
    public String password;

    @NotBlank(message = "Username must not be blank")
    @Size(min = ValidationConstraints.USERNAME_MIN_LENGTH, max = ValidationConstraints.USERNAME_MAX_LENGTH,
            message = "Username must be between {min} and {max} characters")
    public String username;

    @Email(message = "Email should be valid")
    public String email;

    public boolean isAdmin;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDTO that = (RegistrationDTO) o;
        return isAdmin == that.isAdmin && Objects.equals(password, that.password) && Objects.equals(username, that.username) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username, email, isAdmin);
    }
}
