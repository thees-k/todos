package k.thees.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.entity.Task;
import k.thees.security.SecurityService;
import k.thees.validation.TaskNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class TaskService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    private SecurityService securityService;

    public List<Task> findAll() {
        return entityManager.createQuery("SELECT t FROM Task t ORDER BY t.id", Task.class).getResultList();
    }

    public Task findByIdOrThrow(Long id) {
        Task task = entityManager.find(Task.class, id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        } else {
            return task;
        }
    }

    public Task create(Task task) {
        setCreatedAtUpdatedAtAndUpdatedBy(task);
        entityManager.persist(task);
        return task;
    }

    private void setCreatedAtUpdatedAtAndUpdatedBy(Task task) {
        LocalDateTime now = LocalDateTime.now();
        task.setUpdatedAt(now);
        task.setCreatedAt(now);
        task.setUpdatedBy(securityService.getLoggedInUserOrThrow());
    }

    public Task update(Task task) {
        setUpdatedAtAndUpdatedBy(task);
        return entityManager.merge(task);
    }

    private void setUpdatedAtAndUpdatedBy(Task task) {
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedBy(securityService.getLoggedInUserOrThrow());
    }

    public void delete(Long id) {
        Task task = findByIdOrThrow(id);
        entityManager.remove(task);
    }
}