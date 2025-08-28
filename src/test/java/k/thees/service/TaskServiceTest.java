package k.thees.service;

import jakarta.persistence.EntityManager;
import k.thees.entity.Role;
import k.thees.entity.Task;
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
import static k.thees.testutil.ValidationUtils.validateIsBetween;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        lenient().when(securityService.getLoggedInUserOrThrow()).thenReturn(createUser(1L, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID));
    }

    @Test
    void create_shouldSetCreatedAtUpdatedAtAndUpdatedByToCurrentUser() {
        Task task = createTask(null, "New Task");

        LocalDateTime beforeCreate = LocalDateTime.now();
        Task createdTask = taskService.create(task);
        LocalDateTime afterCreate = LocalDateTime.now();

        validateIsBetween(createdTask.getCreatedAt(), beforeCreate, afterCreate);
        validateIsBetween(createdTask.getUpdatedAt(), beforeCreate, afterCreate);

        assertNotNull(createdTask.getUpdatedBy());
        assertEquals("Alice", createdTask.getUpdatedBy().getUsername());
    }

    @Test
    void update_shouldSetUpdatedAtAndUpdatedByToCurrentUser() {
        Task task = createTask(1L, "Task");
        LocalDateTime beforeUpdate = LocalDateTime.now();

        when(entityManager.merge(task)).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = taskService.update(task);
        LocalDateTime afterUpdate = LocalDateTime.now();

        verify(entityManager).merge(task);
        assertNotNull(updatedTask.getUpdatedAt());
        assertFalse(updatedTask.getUpdatedAt().isBefore(beforeUpdate));
        assertFalse(updatedTask.getUpdatedAt().isAfter(afterUpdate));

        assertNotNull(updatedTask.getUpdatedBy());
        assertEquals("Alice", updatedTask.getUpdatedBy().getUsername());
    }

    @Test
    void update_shouldNotChangeCreatedAt() {
        Task task = createTask(1L, "Task");
        LocalDateTime originalCreatedAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        task.setCreatedAt(originalCreatedAt);

        when(entityManager.merge(task)).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = taskService.update(task);

        assertEquals(originalCreatedAt, updatedTask.getCreatedAt(), "createdAt should remain unchanged after update");
    }

    private Task createTask(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription("Description for " + title);
        task.setDone(false);
        task.setUpdatedBy(new User());
        task.setTodoLists(new HashSet<>());
        return task;
    }
}