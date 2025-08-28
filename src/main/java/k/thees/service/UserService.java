package k.thees.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import k.thees.entity.User;
import k.thees.security.SecurityService;
import k.thees.security.UserNotAdminException;
import k.thees.security.UserNotFoundException;
import k.thees.security.UsernameAlreadyExistsException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    private SecurityService securityService;

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
    }

    public User findByIdOrThrow(Long id) throws UserNotFoundException {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new UserNotFoundException(id);
        } else {
            return user;
        }
    }

    public User create(User user) {
        setCreatedAtUpdatedAtAndUpdatedBy(user);
        entityManager.persist(user);
        return user;
    }

    private void setCreatedAtUpdatedAtAndUpdatedBy(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now);
        user.setCreatedAt(now);
        User admin = securityService.getLoggedInAdminUserOrThrow();
        user.setUpdatedBy(admin);
    }

    public void delete(Long id) {
        User user = findByIdOrThrow(id);
        User currentUser = securityService.getLoggedInUserOrThrow();
        if (currentUser.equals(user) || currentUser.isAdministrator()) {
            entityManager.remove(user);
        } else {
            throw new UserNotAdminException();
        }
    }

    public Optional<User> findByUsername(String username) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            return Optional.of(user);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    public User update(User user) {
        User storedUser = findByIdOrThrow(user.getId());

        checkUsernameExistence(user);

        User currentUser = securityService.getLoggedInUserOrThrow();
        checkAdminPermission(currentUser, storedUser, user);

        storedUser.setUsername(user.getUsername());
        storedUser.setEmail(user.getEmail());
        storedUser.setRole(user.getRole());
        storedUser.setPasswordHash(user.getPasswordHash());
        storedUser.setUpdatedBy(currentUser);
        storedUser.setUpdatedAt(LocalDateTime.now());

        return entityManager.merge(storedUser);
    }

    private void checkUsernameExistence(User user) throws UsernameAlreadyExistsException {
        findByUsername(user.getUsername())
                .filter(loadedUser -> !loadedUser.equals(user))
                .ifPresent(loadedUser -> {
                    throw new UsernameAlreadyExistsException(user.getUsername());
                });
    }

    private void checkAdminPermission(User currentUser, User storedUser, User updatedUser) throws UserNotAdminException {

        boolean isRoleChanged = !storedUser.getRole().equals(updatedUser.getRole());
        boolean isDifferentUser = !currentUser.equals(storedUser);
        if ((isDifferentUser || isRoleChanged) && !currentUser.isAdministrator()) {
            throw new UserNotAdminException();
        }
    }
}
