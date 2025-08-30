package k.thees.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import k.thees.validation.ValidationConstraints;

import java.time.LocalDateTime;
import java.util.Objects;

public class TodoListDTO {
    public Long id;
    public Long ownerId;

    @NotBlank(message = "Name must not be blank")
    @Size(min = ValidationConstraints.TODO_LIST_NAME_MIN_LENGTH, max = ValidationConstraints.TODO_LIST_NAME_MAX_LENGTH,
            message = "Name must be between {min} and {max} characters")
    public String name;

    @NotNull
    public String description;

    @NotNull
    public Boolean isPublic;

    @NotNull
    public Boolean isDone;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Long updatedById;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TodoListDTO that = (TodoListDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(ownerId, that.ownerId) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(isPublic, that.isPublic) && Objects.equals(isDone, that.isDone) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(updatedById, that.updatedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, name, description, isPublic, isDone, createdAt, updatedAt, updatedById);
    }
}