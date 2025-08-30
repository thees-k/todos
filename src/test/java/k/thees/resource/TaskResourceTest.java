package k.thees.resource;

import jakarta.ws.rs.core.Response;
import k.thees.dto.SaveTaskDTO;
import k.thees.dto.TaskDTO;
import k.thees.entity.Task;
import k.thees.entity.TodoList;
import k.thees.mapper.TaskMapper;
import k.thees.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskResourceTest {

    @InjectMocks
    private TaskResource taskResource;

    @Mock
    private TaskService taskService;

    @Test
    void getAllTasks_returnsTaskDTOList() {
        Task task1 = createTask(1L, "Task 1");
        Task task2 = createTask(2L, "Task 2");
        when(taskService.findAll()).thenReturn(List.of(task1, task2));
        List<TaskDTO> expectedTasks = List.of(TaskMapper.toDTO(task1), TaskMapper.toDTO(task2));

        Response response = taskResource.getAllTasks();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<TaskDTO> result = (List<TaskDTO>) response.getEntity();
        assertEquals(expectedTasks, result);
        verify(taskService).findAll();
    }

    @Test
    void getTask_existingId_returnsOkResponseWithTaskDTO() {
        Task task = createTask(1L, "Task 1");
        TaskDTO expectedDTO = TaskMapper.toDTO(task);
        when(taskService.findByIdOrThrow(1L)).thenReturn(task);

        Response response = taskResource.getTask(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TaskDTO dto = (TaskDTO) response.getEntity();
        assertEquals(expectedDTO, dto);
        verify(taskService).findByIdOrThrow(1L);
    }

    @Test
    void createTask_shouldReturnCreatedResponseWithLocationAndEntity() {
        SaveTaskDTO inputDTO = new SaveTaskDTO();
        inputDTO.title = "New Task";
        inputDTO.todoListId = 1L;
        TaskMapper.toEntity(inputDTO);

        Task createdTask = createTask(1L, "New Task");
        TaskDTO expectedDTO = TaskMapper.toDTO(createdTask);
        when(taskService.create(any(Task.class))).thenReturn(createdTask);

        TaskDTO returnedDTO;
        try (Response response = taskResource.createTask(inputDTO)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(URI.create("/api/tasks/1"), response.getLocation());
            returnedDTO = (TaskDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedDTO, returnedDTO);
        verify(taskService).create(any(Task.class));
    }

    @Test
    void updateTask_shouldReturnOkResponseWithUpdatedTaskDTO() {
        SaveTaskDTO inputDTO = new SaveTaskDTO();
        inputDTO.title = "Updated Task";
        inputDTO.todoListId = 1L;
        TaskMapper.toEntity(inputDTO);

        Task updatedTask = createTask(1L, "Updated Task");
        TaskDTO expectedDTO = TaskMapper.toDTO(updatedTask);
        when(taskService.update(any(Task.class))).thenReturn(updatedTask);

        TaskDTO returnedDTO;
        try (Response response = taskResource.updateTask(1L, inputDTO)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            returnedDTO = (TaskDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedDTO, returnedDTO);
        verify(taskService).update(any(Task.class));
    }

    @Test
    void deleteTask_existingId_returnsNoContent() {
        when(taskService.delete(1L)).thenReturn(true);

        try (Response response = taskResource.deleteTask(1L)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(taskService).delete(1L);
    }

    @Test
    void deleteTask_nonExistingId_returnsNotFound() {
        when(taskService.delete(99L)).thenReturn(false);

        try (Response response = taskResource.deleteTask(99L)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(taskService).delete(99L);
    }

    private Task createTask(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setTodoList(new TodoList(1L));
        return task;
    }
}