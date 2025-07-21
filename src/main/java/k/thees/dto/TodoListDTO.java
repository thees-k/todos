package k.thees.dto;

import java.time.LocalDateTime;
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
}