package k.thees.mapper;

import k.thees.dto.TaskDTO;
import k.thees.entity.Task;
import k.thees.entity.User;

public class TaskMapper {

    public static TaskDTO toDTO(Task task) {
        if (task == null) return null;
        TaskDTO dto = new TaskDTO();
        dto.id = task.getId();
        dto.title = task.getTitle();
        dto.description = task.getDescription();
        dto.done = task.getDone();
        dto.createdAt = task.getCreatedAt();
        dto.updatedAt = task.getUpdatedAt();
        dto.updatedById = task.getUpdatedBy() != null ? task.getUpdatedBy().getId() : null;
        return dto;
    }

    public static Task toEntity(TaskDTO dto) {
        if (dto == null) return null;
        Task task = new Task();
        task.setId(dto.id);
        task.setTitle(dto.title);
        task.setDescription(dto.description);
        task.setDone(dto.done);
        // createdAt and updatedAt usually managed by DB or service, set if needed
        if (dto.updatedById != null) {
            User updatedBy = new User();
            updatedBy.setId(dto.updatedById);
            task.setUpdatedBy(updatedBy);
        }
        return task;
    }
}