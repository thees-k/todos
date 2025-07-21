package k.thees.mapper;

import k.thees.dto.UserDTO;
import k.thees.entity.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.passwordHash = user.getPasswordHash();
        dto.updatedAt = user.getUpdatedAt();
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.id);
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPasswordHash(dto.passwordHash);
        // updatedAt usually managed by DB or service, set if needed
        return user;
    }
}