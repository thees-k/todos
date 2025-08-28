package k.thees.service;

import jakarta.persistence.EntityManager;
import k.thees.entity.Role;
import k.thees.entity.TodoList;
import k.thees.entity.User;
import k.thees.security.SecurityService;
import k.thees.security.TodoListNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;

import static k.thees.testutil.TestDataFactory.createUser;
import static k.thees.testutil.ValidationUtils.validateIsBetween;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoListServiceTest {

    @InjectMocks
    private TodoListService todoListService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        lenient().when(securityService.getLoggedInUserOrThrow()).thenReturn(createUser(1L, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID));
    }

    @Test
    void findByIdOrThrow_shouldReturnTodoList_whenFound() {
        TodoList todoList = createTodoList(1L, "List 1", "Description", true, false);
        when(entityManager.find(TodoList.class, 1L)).thenReturn(todoList);

        TodoList result = todoListService.findByIdOrThrow(1L);

        assertEquals(todoList, result);
        verify(entityManager).find(TodoList.class, 1L);
    }

    @Test
    void findByIdOrThrow_shouldThrowTodoListNotFoundException_whenNotFound() {
        when(entityManager.find(TodoList.class, 99L)).thenReturn(null);

        TodoListNotFoundException ex = assertThrows(TodoListNotFoundException.class, () -> todoListService.findByIdOrThrow(99L));
        assertEquals("TodoList with ID 99 not found", ex.getMessage());
        verify(entityManager).find(TodoList.class, 99L);
    }

    @Test
    void create_shouldSetCreatedAtUpdatedAtAndUpdatedByToCurrentUser() {
        TodoList todoList = createTodoList(null, "New List", "Description", false, false);

        LocalDateTime beforeCreate = LocalDateTime.now();
        TodoList createdTodoList = todoListService.create(todoList);
        LocalDateTime afterCreate = LocalDateTime.now();

        validateIsBetween(createdTodoList.getCreatedAt(), beforeCreate, afterCreate);
        validateIsBetween(createdTodoList.getUpdatedAt(), beforeCreate, afterCreate);

        assertNotNull(createdTodoList.getUpdatedBy());
        assertEquals("Alice", createdTodoList.getUpdatedBy().getUsername());
    }

    @Test
    void create_shouldSetEqualCreatedAtAndUpdatedAt() {
        TodoList todoList = createTodoList(null, "New List", "Description", false, false);
        TodoList createdTodoList = todoListService.create(todoList);
        assertEquals(createdTodoList.getCreatedAt(), createdTodoList.getUpdatedAt());
    }

    @Test
    void update_shouldSetUpdatedAtAndUpdatedByToCurrentUser() {
        TodoList todoList = createTodoList(1L, "List", "Description", false, false);

        LocalDateTime beforeUpdate = LocalDateTime.now();
        when(entityManager.merge(todoList)).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updatedTodoList = todoListService.update(todoList);
        LocalDateTime afterUpdate = LocalDateTime.now();

        verify(entityManager).merge(todoList);
        validateIsBetween(updatedTodoList.getUpdatedAt(), beforeUpdate, afterUpdate);

        assertNotNull(updatedTodoList.getUpdatedBy());
        assertEquals("Alice", updatedTodoList.getUpdatedBy().getUsername());
    }

    @Test
    void update_shouldNotChangeCreatedAt() {
        TodoList todoList = createTodoList(1L, "List", "Description", false, false);
        LocalDateTime originalCreatedAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        todoList.setCreatedAt(originalCreatedAt);

        when(entityManager.merge(todoList)).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updatedTodoList = todoListService.update(todoList);

        assertEquals(originalCreatedAt, updatedTodoList.getCreatedAt(), "createdAt should remain unchanged after update");
    }

    private TodoList createTodoList(Long id, String name, String description, Boolean isPublic, Boolean isDone) {
        TodoList todoList = new TodoList();
        todoList.setId(id);
        todoList.setName(name);
        todoList.setDescription(description);
        todoList.setPublic(isPublic);
        todoList.setDone(isDone);
        todoList.setOwner(new User());
        todoList.setUpdatedBy(new User());
        todoList.setTasks(new HashSet<>());
        return todoList;
    }
}