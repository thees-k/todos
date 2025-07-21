package k.thees.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import k.thees.entity.Task;
import k.thees.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private EntityManager entityManager;

    @Test
    void findAll_shouldReturnListOfTasks() {
        Task task1 = createTask(1L, "Task 1");
        Task task2 = createTask(2L, "Task 2");
        List<Task> mockTasks = Arrays.asList(task1, task2);

        TypedQuery<Task> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT t FROM Task t ORDER BY t.id", Task.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockTasks);

        assertEquals(mockTasks, taskService.findAll());
    }

    @Test
    void findById_existingId_shouldReturnTask() {
        Task task = createTask(1L, "Task 1");
        when(entityManager.find(Task.class, 1L)).thenReturn(task);

        Optional<Task> result = taskService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(task, result.get());
    }

    @Test
    void findById_nonExistingId_shouldReturnEmptyOptional() {
        long id = 99L;
        when(entityManager.find(Task.class, id)).thenReturn(null);

        Optional<Task> result = taskService.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void create_shouldPersistAndReturnTask() {
        Task task = createTask(null, "New Task");

        Task result = taskService.create(task);

        verify(entityManager).persist(task);
        assertEquals(task, result);
    }

    @Test
    void update_shouldMergeAndReturnUpdatedTask() {
        Task task = createTask(1L, "Updated Task");
        when(entityManager.merge(task)).thenReturn(task);

        Task result = taskService.update(task);

        verify(entityManager).merge(task);
        assertEquals(task, result);
    }

    @Test
    void delete_existingId_shouldRemoveTaskAndReturnTrue() {
        Task task = createTask(1L, "ToDelete");
        when(entityManager.find(Task.class, task.getId())).thenReturn(task);

        boolean result = taskService.delete(task.getId());

        verify(entityManager).remove(task);
        assertTrue(result);
    }

    @Test
    void delete_nonExistingId_shouldReturnFalse() {
        long id = 99L;
        when(entityManager.find(Task.class, id)).thenReturn(null);

        boolean result = taskService.delete(id);

        verify(entityManager, never()).remove(any());
        assertFalse(result);
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