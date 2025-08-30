package k.thees.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.entity.TodoList;
import k.thees.security.SecurityService;
import k.thees.validation.TodoListNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class TodoListService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    private SecurityService securityService;

    public List<TodoList> findAll() {
        return entityManager.createQuery("SELECT t FROM TodoList t ORDER BY t.id", TodoList.class).getResultList();
    }

    public TodoList findByIdOrThrow(Long id) throws TodoListNotFoundException {
        TodoList todoList = entityManager.find(TodoList.class, id);
        if (todoList == null) {
            throw new TodoListNotFoundException(id);
        } else {
            return todoList;
        }
    }

    public TodoList create(TodoList todoList) {
        setCreatedAtUpdatedAtAndUpdatedBy(todoList);
        entityManager.persist(todoList);
        return todoList;
    }

    private void setCreatedAtUpdatedAtAndUpdatedBy(TodoList todoList) {
        LocalDateTime now = LocalDateTime.now();
        todoList.setCreatedAt(now);
        todoList.setUpdatedAt(now);
        todoList.setUpdatedBy(securityService.getLoggedInUserOrThrow());
    }

    public TodoList update(TodoList todoList) {
        setUpdatedAtAndUpdatedBy(todoList);
        return entityManager.merge(todoList);
    }

    private void setUpdatedAtAndUpdatedBy(TodoList todoList) {
        todoList.setUpdatedAt(LocalDateTime.now());
        todoList.setUpdatedBy(securityService.getLoggedInUserOrThrow());
    }

    public void delete(Long id) {
        TodoList todoList = findByIdOrThrow(id);
        entityManager.remove(todoList);
    }
}
