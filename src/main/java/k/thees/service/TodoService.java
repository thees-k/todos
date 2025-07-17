package k.thees.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.model.Todo;

import java.util.List;

@ApplicationScoped
@Transactional
public class TodoService {

    @PersistenceContext()
    private EntityManager entityManager;

    public List<Todo> getAllTodos() {
        return entityManager.createQuery("SELECT t FROM Todo t", Todo.class).getResultList();
    }

    public Todo getTodoById(Long id) {
        return new Todo(id, "Play piano", "Two new pieces of Brahms", false);
    }

    public Todo createTodo(Todo todo) {
        entityManager.persist(todo);
        return todo;
    }

    public boolean updateTodo(Long id, Todo todo) {
        entityManager.merge(todo);
        return true;
    }

    public boolean deleteTodo(Long id) {
        entityManager.remove(entityManager.find(Todo.class, id));
        return true;
    }
}
