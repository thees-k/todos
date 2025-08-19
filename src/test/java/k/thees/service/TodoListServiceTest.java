package k.thees.service;

import jakarta.persistence.EntityManager;
import k.thees.entity.Role;
import k.thees.entity.TodoList;
import k.thees.entity.User;
import k.thees.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;

import static k.thees.testutil.TestDataFactory.createUser;
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
        lenient().when(securityService.getLoggedInUserOrThrow()).thenReturn(createUser(1L, "Alice", "alice@example.com", "hash1", Role.RoleType.REGULAR_USER));
    }

    @Test
    void create_shouldSetCreatedAtUpdatedAtAndUpdatedByToCurrentUser() {
        TodoList todoList = createTodoList(null, "New List", "Description", false, false);

        LocalDateTime beforeCreate = LocalDateTime.now();
        TodoList createdTodoList = todoListService.create(todoList);
        LocalDateTime afterCreate = LocalDateTime.now();

        assertNotNull(createdTodoList.getCreatedAt());
        assertNotNull(createdTodoList.getUpdatedAt());
        assertFalse(createdTodoList.getCreatedAt().isBefore(beforeCreate));
        assertFalse(createdTodoList.getCreatedAt().isAfter(afterCreate));
        assertFalse(createdTodoList.getUpdatedAt().isBefore(beforeCreate));
        assertFalse(createdTodoList.getUpdatedAt().isAfter(afterCreate));

        assertNotNull(createdTodoList.getUpdatedBy());
        assertEquals("Alice", createdTodoList.getUpdatedBy().getUsername());
    }

    @Test
    void update_shouldSetUpdatedAtAndUpdatedByToCurrentUser() {
        TodoList todoList = createTodoList(1L, "List", "Description", false, false);

        LocalDateTime beforeUpdate = LocalDateTime.now();
        when(entityManager.merge(todoList)).thenAnswer(invocation -> invocation.getArgument(0));

        TodoList updatedTodoList = todoListService.update(todoList);
        LocalDateTime afterUpdate = LocalDateTime.now();

        verify(entityManager).merge(todoList);
        assertNotNull(updatedTodoList.getUpdatedAt());
        assertFalse(updatedTodoList.getUpdatedAt().isBefore(beforeUpdate));
        assertFalse(updatedTodoList.getUpdatedAt().isAfter(afterUpdate));

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