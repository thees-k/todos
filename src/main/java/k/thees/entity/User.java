package k.thees.entity;

import jakarta.persistence.*;
import k.thees.validation.ValidationConstraints;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(
            name = "users_seq",             // JPA generator name
            sequenceName = "users_seq",     // actual DB sequence name
            allocationSize = 1            // matches DB sequence increment
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Long id;

    @Column(nullable = false, unique = true, length = ValidationConstraints.USERNAME_MAX_LENGTH)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne // many users can have the same (one) role
    @JoinColumn(name = "role", nullable = false)
    private Role role;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isAdministrator() {
        return this.role.getId().equals(Role.ADMINISTRATOR_ID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
