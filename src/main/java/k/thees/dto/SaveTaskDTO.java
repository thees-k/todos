package k.thees.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import k.thees.validation.ValidationConstraints;

import java.util.Objects;

public class SaveTaskDTO {

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SaveTaskDTO that = (SaveTaskDTO) o;
        return Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(todoListId, that.todoListId) && Objects.equals(priority, that.priority) && Objects.equals(done, that.done);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, todoListId, priority, done);
    }
}