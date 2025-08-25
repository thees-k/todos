package k.thees.mapper;

import k.thees.dto.RoleDTO;
import k.thees.entity.Role;

public class RoleMapper {
    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;
        RoleDTO dto = new RoleDTO();
        dto.id = role.getId();
        dto.name = role.getName();
        return dto;
    }

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setId(dto.id);
        role.setName(dto.name);
        return role;
    }
}
