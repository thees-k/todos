package k.thees.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class TodoListDTO {
    public Long id;
    public Long ownerId;
    public String name;
    public String description;
    public Boolean isPublic;
    public Boolean isDone;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Long updatedById;
    public Set<Long> taskIds;

    public TodoListDTO() {}

    public TodoListDTO(Long id, Long ownerId, String name, String description, Boolean isPublic, Boolean isDone,
                       LocalDateTime createdAt, LocalDateTime updatedAt, Long updatedById, Set<Long> taskIds) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isDone = isDone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedById = updatedById;
        this.taskIds = taskIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TodoListDTO that = (TodoListDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(ownerId, that.ownerId) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(isPublic, that.isPublic) && Objects.equals(isDone, that.isDone) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(updatedById, that.updatedById) && Objects.equals(taskIds, that.taskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, name, description, isPublic, isDone, createdAt, updatedAt, updatedById, taskIds);
    }
}