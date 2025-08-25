package k.thees.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.entity.Role;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class RoleService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r ORDER BY r.id", Role.class).getResultList();
    }

    public Optional<Role> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Role.class, id));
    }
}
