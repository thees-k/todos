package k.thees.mapper;

import k.thees.dto.TodoListDTO;
import k.thees.entity.Task;
import k.thees.entity.TodoList;
import k.thees.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class TodoListMapper {

    public static TodoListDTO toDTO(TodoList todoList) {
        if (todoList == null) return null;
        TodoListDTO dto = new TodoListDTO();
        dto.id = todoList.getId();
        dto.ownerId = todoList.getOwner() != null ? todoList.getOwner().getId() : null;
        dto.name = todoList.getName();
        dto.description = todoList.getDescription();
        dto.isPublic = todoList.getPublic();
        dto.isDone = todoList.getDone();
        dto.createdAt = todoList.getCreatedAt();
        dto.updatedAt = todoList.getUpdatedAt();
        dto.updatedById = todoList.getUpdatedBy() != null ? todoList.getUpdatedBy().getId() : null;
        Set<Task> tasks = todoList.getTasks();
        if (tasks != null) {
            dto.taskIds = tasks.stream()
                               .map(Task::getId)
                               .collect(Collectors.toSet());
        }
        return dto;
    }

    public static TodoList toEntity(TodoListDTO dto) {
        if (dto == null) return null;
        TodoList todoList = new TodoList();
        todoList.setId(dto.id);
        if (dto.ownerId != null) {
            User owner = new User();
            owner.setId(dto.ownerId);
            todoList.setOwner(owner);
        }
        todoList.setName(dto.name);
        todoList.setDescription(dto.description);
        todoList.setPublic(dto.isPublic);
        todoList.setDone(dto.isDone);
        todoList.setCreatedAt(dto.createdAt);
        todoList.setUpdatedAt(dto.updatedAt);
        if (dto.updatedById != null) {
            User updatedBy = new User();
            updatedBy.setId(dto.updatedById);
            todoList.setUpdatedBy(updatedBy);
        }
        // Tasks are not set here because DTO only contains task IDs; setting tasks requires fetching entities
        return todoList;
    }
}