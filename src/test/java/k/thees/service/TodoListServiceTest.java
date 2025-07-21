package k.thees.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import k.thees.entity.TodoList;
import k.thees.entity.User;
import org.junit.jupiter.api.BeforeEach;
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
class TodoListServiceTest {

    @InjectMocks
    private TodoListService todoListService;

    @Mock
    private EntityManager entityManager;

    @Test
    void findAll_shouldReturnListOfTodoLists() {
        TodoList list1 = createTodoList(1L, "List 1", "Desc 1", false, false);
        TodoList list2 = createTodoList(2L, "List 2", "Desc 2", true, true);
        List<TodoList> mockLists = Arrays.asList(list1, list2);

        TypedQuery<TodoList> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT t FROM TodoList t ORDER BY t.id", TodoList.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockLists);

        assertEquals(mockLists, todoListService.findAll());
    }

    @Test
    void findById_existingId_shouldReturnTodoList() {
        TodoList list = createTodoList(1L, "List 1", "Desc 1", false, false);
        when(entityManager.find(TodoList.class, 1L)).thenReturn(list);

        Optional<TodoList> result = todoListService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(list, result.get());
    }

    @Test
    void findById_nonExistingId_shouldReturnEmptyOptional() {
        long id = 99L;
        when(entityManager.find(TodoList.class, id)).thenReturn(null);

        Optional<TodoList> result = todoListService.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void create_shouldPersistAndReturnTodoList() {
        TodoList list = createTodoList(1L, "List 1", "Desc 1", false, false);

        TodoList result = todoListService.create(list);

        verify(entityManager).persist(list);
        assertEquals(list, result);
    }

    @Test
    void update_shouldMergeAndReturnUpdatedTodoList() {
        TodoList list = createTodoList(1L, "List 1", "Desc 1", false, false);
        when(entityManager.merge(list)).thenReturn(list);

        TodoList result = todoListService.update(list);

        verify(entityManager).merge(list);
        assertEquals(list, result);
    }

    @Test
    void delete_existingId_shouldRemoveTodoListAndReturnTrue() {
        TodoList list = createTodoList(1L, "List 1", "Desc 1", false, false);
        when(entityManager.find(TodoList.class, list.getId())).thenReturn(list);

        boolean result = todoListService.delete(list.getId());

        verify(entityManager).remove(list);
        assertTrue(result);
    }

    @Test
    void delete_nonExistingId_shouldReturnFalse() {
        long id = 99L;
        when(entityManager.find(TodoList.class, id)).thenReturn(null);

        boolean result = todoListService.delete(id);

        verify(entityManager, never()).remove(any());
        assertFalse(result);
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