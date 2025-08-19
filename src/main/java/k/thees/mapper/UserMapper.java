package k.thees.mapper;

import k.thees.dto.UserDTO;
import k.thees.entity.Role;
import k.thees.entity.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.roleId = user.getRole().getId();
        dto.passwordHash = user.getPasswordHash();
        dto.createdAt = user.getCreatedAt();
        dto.updatedById = user.getUpdatedBy() == null ? null : user.getUpdatedBy().getId();
        dto.updatedAt = user.getUpdatedAt();
        return dto;
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.id);
        user.setUsername(dto.username);
        user.setEmail(dto.email);

        Role role = new Role();
        role.setId(dto.roleId);
        user.setRole(role);

        user.setPasswordHash(dto.passwordHash);
        user.setCreatedAt(dto.createdAt);
        if (dto.updatedById != null) {
            User updatedBy = new User();
            updatedBy.setId(dto.updatedById);
            user.setUpdatedBy(updatedBy);
        }
        user.setUpdatedAt(dto.updatedAt);
        return user;
    }
}