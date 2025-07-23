package k.thees.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class TaskDTO {
    public Long id;
    public String title;
    public String description;
    public Boolean done;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Long updatedById;

    public TaskDTO() {}

    public TaskDTO(Long id, String title, String description, Boolean done, LocalDateTime createdAt, LocalDateTime updatedAt, Long updatedById) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedById = updatedById;
    }

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