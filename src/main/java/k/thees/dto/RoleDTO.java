package k.thees.dto;

import java.util.Objects;

public class RoleDTO {
    public Integer id;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(id, roleDTO.id) && Objects.equals(name, roleDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
