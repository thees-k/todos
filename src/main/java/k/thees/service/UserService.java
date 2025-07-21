package k.thees.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.entity.User;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    public User update(User user) {
        return entityManager.merge(user);
    }

    public boolean delete(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
            return true;
        }
        return false;
    }
}
