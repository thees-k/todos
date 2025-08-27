package k.thees.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import k.thees.validation.ValidationConstraints;

import java.util.Objects;

public class CreateTodoListDTO {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateTodoListDTO that = (CreateTodoListDTO) o;
        return Objects.equals(ownerId, that.ownerId) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(isPublic, that.isPublic) && Objects.equals(isDone, that.isDone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, name, description, isPublic, isDone);
    }
}