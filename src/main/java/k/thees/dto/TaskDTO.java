package k.thees.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import k.thees.validation.ValidationConstraints;

import java.time.LocalDateTime;
import java.util.Objects;

public class TaskDTO {
    public Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(min = ValidationConstraints.TASK_TITLE_MIN_LENGTH, max = ValidationConstraints.TASK_TITLE_MAX_LENGTH,
            message = "Title must be between {min} and {max} characters")
    public String title;

    @NotNull
    public String description;

    @NotNull
    public Long todoListId;

    @NotNull
    public Integer priority;

    @NotNull
    public Boolean done;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Long updatedById;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskDTO taskDTO = (TaskDTO) o;
        return Objects.equals(id, taskDTO.id) && Objects.equals(title, taskDTO.title) && Objects.equals(description, taskDTO.description) && Objects.equals(done, taskDTO.done) && Objects.equals(createdAt, taskDTO.createdAt) && Objects.equals(updatedAt, taskDTO.updatedAt) && Objects.equals(updatedById, taskDTO.updatedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, done, createdAt, updatedAt, updatedById);
    }
}