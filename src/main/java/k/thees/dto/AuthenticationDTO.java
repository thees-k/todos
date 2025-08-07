package k.thees.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationDTO {

    @NotBlank(message = "Username must not be blank")
    public String username;

    @NotBlank(message = "Password must not be blank")
    public String password;
}
