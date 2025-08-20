package k.thees.entity;

import jakarta.persistence.*;
import k.thees.validation.ValidationConstraints;

import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {

    // These constants must be in sync with the records in the database table "roles"
    public static int ADMINISTRATOR_ID = 1;
    public static int REGULAR_USER_ID = 2;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role() {
    }

    public Role(int roleId) {
        id = roleId;
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
