package k.thees.entity;

import jakarta.persistence.*;
import k.thees.validation.ValidationConstraints;

import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @SequenceGenerator(
            name = "roles_seq",
            sequenceName = "roles_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
    private Integer id;

    @Column(nullable = false, unique = true, length = ValidationConstraints.ROLE_NAME_MAX_LENGTH)
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
