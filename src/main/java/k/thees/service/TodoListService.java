package k.thees.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.entity.TodoList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class TodoListService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    public List<TodoList> findAll() {
        return entityManager.createQuery("SELECT t FROM TodoList t ORDER BY t.id", TodoList.class).getResultList();
    }

    public Optional<TodoList> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TodoList.class, id));
    }

    public TodoList create(TodoList todoList) {
        LocalDateTime now = LocalDateTime.now();
        todoList.setCreatedAt(now);
        todoList.setUpdatedAt(now);
        entityManager.persist(todoList);
        return todoList;
    }

    public TodoList update(TodoList todoList) {
        todoList.setUpdatedAt(LocalDateTime.now());
        return entityManager.merge(todoList);
    }

    public boolean delete(Long id) {
        TodoList todoList = entityManager.find(TodoList.class, id);
        if (todoList != null) {
            entityManager.remove(todoList);
            return true;
        }
        return false;
    }
}
