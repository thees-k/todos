package k.thees.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.entity.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class TaskService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    public List<Task> findAll() {
        return entityManager.createQuery("SELECT t FROM Task t ORDER BY t.id", Task.class).getResultList();
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Task.class, id));
    }

    public Task create(Task task) {
        LocalDateTime now = LocalDateTime.now();
        task.setUpdatedAt(now);
        task.setCreatedAt(now);
        entityManager.persist(task);
        return task;
    }

    public Task update(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        return entityManager.merge(task);
    }

    public boolean delete(Long id) {
        Task task = entityManager.find(Task.class, id);
        if (task != null) {
            entityManager.remove(task);
            return true;
        }
        return false;
    }
}