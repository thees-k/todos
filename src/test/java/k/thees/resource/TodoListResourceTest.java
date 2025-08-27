package k.thees.resource;

import jakarta.ws.rs.core.Response;
import k.thees.dto.CreateTodoListDTO;
import k.thees.dto.TodoListDTO;
import k.thees.entity.TodoList;
import k.thees.mapper.TodoListMapper;
import k.thees.service.TodoListService;
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
class TodoListResourceTest {

    @InjectMocks
    private TodoListResource todoListResource;

    @Mock
    private TodoListService todoListService;

    @Test
    void getAllTodoLists_returnsTodoListDTOList() {
        TodoList list1 = createTodoList(1L, "List 1");
        TodoList list2 = createTodoList(2L, "List 2");
        when(todoListService.findAll()).thenReturn(List.of(list1, list2));
        List<TodoListDTO> expectedLists = List.of(TodoListMapper.toDTO(list1), TodoListMapper.toDTO(list2));

        Response response = todoListResource.getAllTodoLists();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<TodoListDTO> result = (List<TodoListDTO>) response.getEntity();
        assertEquals(expectedLists, result);
        verify(todoListService).findAll();
    }

    @Test
    void getTodoList_existingId_returnsOkResponseWithTodoListDTO() {
        TodoList todoList = createTodoList(1L, "List 1");
        TodoListDTO expectedDTO = TodoListMapper.toDTO(todoList);
        when(todoListService.findByIdOrThrow(1L)).thenReturn(todoList);

        Response response = todoListResource.getTodoList(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TodoListDTO dto = (TodoListDTO) response.getEntity();
        assertEquals(expectedDTO, dto);
        verify(todoListService).findByIdOrThrow(1L);
    }

    @Test
    void createTodoList_shouldReturnCreatedResponseWithLocationAndEntity() {

        CreateTodoListDTO inputDTO = new CreateTodoListDTO();
        inputDTO.name = "New List";

        TodoList createdList = createTodoList(1L, "New List");
        TodoListDTO expectedDTO = TodoListMapper.toDTO(createdList);

        when(todoListService.create(any(TodoList.class))).thenReturn(createdList);

        TodoListDTO returnedDTO;
        try (Response response = todoListResource.createTodoList(inputDTO)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(URI.create("/api/todolists/1"), response.getLocation());
            returnedDTO = (TodoListDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedDTO, returnedDTO);
        verify(todoListService).create(any(TodoList.class));
    }

    @Test
    void updateTodoList_shouldReturnOkResponseWithUpdatedTodoListDTO() {
        TodoListDTO inputDTO = new TodoListDTO();
        inputDTO.name = "Updated List";
        TodoListMapper.toEntity(inputDTO);

        TodoList updatedList = createTodoList(1L, "Updated List");
        TodoListDTO expectedDTO = TodoListMapper.toDTO(updatedList);
        when(todoListService.update(any(TodoList.class))).thenReturn(updatedList);

        TodoListDTO returnedDTO;
        try (Response response = todoListResource.updateTodoList(1L, inputDTO)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            returnedDTO = (TodoListDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedDTO, returnedDTO);
        verify(todoListService).update(any(TodoList.class));
    }

    @Test
    void deleteTodoList_existingId_returnsNoContent() {
        when(todoListService.delete(1L)).thenReturn(true);

        try (Response response = todoListResource.deleteTodoList(1L)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(todoListService).delete(1L);
    }

    @Test
    void deleteTodoList_nonExistingId_returnsNotFound() {
        when(todoListService.delete(99L)).thenReturn(false);

        try (Response response = todoListResource.deleteTodoList(99L)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(todoListService).delete(99L);
    }

    private TodoList createTodoList(Long id, String name) {
        TodoList todoList = new TodoList();
        todoList.setId(id);
        todoList.setName(name);
        return todoList;
    }
}