package k.thees.mapper;

import k.thees.dto.CreateTodoListDTO;
import k.thees.dto.TodoListDTO;
import k.thees.dto.UpdateTodoListDTO;
import k.thees.entity.TodoList;
import k.thees.entity.User;

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
        return dto;
    }

    public static TodoList toEntity(CreateTodoListDTO dto) {
        if (dto == null) return null;
        TodoList todoList = new TodoList();
        if (dto.ownerId != null) {
            User owner = new User(dto.ownerId);
            todoList.setOwner(owner);
        }
        todoList.setName(dto.name);
        todoList.setDescription(dto.description);
        todoList.setPublic(dto.isPublic);
        todoList.setDone(dto.isDone);
        return todoList;
    }

    public static TodoList toEntity(UpdateTodoListDTO dto) {
        if (dto == null) return null;
        TodoList todoList = new TodoList();
        if (dto.ownerId != null) {
            User owner = new User(dto.ownerId);
            todoList.setOwner(owner);
        }
        todoList.setName(dto.name);
        todoList.setDescription(dto.description);
        todoList.setPublic(dto.isPublic);
        todoList.setDone(dto.isDone);
        return todoList;
    }
}