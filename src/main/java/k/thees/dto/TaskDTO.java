package k.thees.dto;

import java.time.LocalDateTime;

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
}