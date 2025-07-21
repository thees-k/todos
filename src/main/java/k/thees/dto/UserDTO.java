package k.thees.dto;

import java.time.LocalDateTime;

public class UserDTO {
    public Long id;
    public String username;
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
}