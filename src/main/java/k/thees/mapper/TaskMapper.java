package k.thees.mapper;

import k.thees.dto.SaveTaskDTO;
import k.thees.dto.TaskDTO;
import k.thees.entity.Task;
import k.thees.entity.TodoList;
import k.thees.entity.User;

public class TaskMapper {

    public static TaskDTO toDTO(Task task) {
        if (task == null) return null;
        TaskDTO dto = new TaskDTO();
        dto.id = task.getId();
        dto.title = task.getTitle();
        dto.description = task.getDescription();
        dto.todoListId = task.getTodoList().getId();
        dto.priority = task.getPriority();
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
        task.setTodoList(new TodoList(dto.todoListId));
        task.setPriority(dto.priority);
        task.setDone(dto.done);
        task.setCreatedAt(dto.createdAt);
        task.setUpdatedAt(dto.updatedAt);
        if (dto.updatedById != null) {
            User updatedBy = new User();
            updatedBy.setId(dto.updatedById);
            task.setUpdatedBy(updatedBy);
        }
        return task;
    }

    public static Task toEntity(SaveTaskDTO dto) {
        if (dto == null) return null;
        Task task = new Task();
        task.setTitle(dto.title);
        task.setDescription(dto.description);
        task.setTodoList(new TodoList(dto.todoListId));
        task.setPriority(dto.priority);
        task.setDone(dto.done);
        return task;
    }
}