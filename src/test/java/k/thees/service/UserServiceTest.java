package k.thees.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import k.thees.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @Test
    void findAll_shouldReturnListOfUsers() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1");
        User bob = createUser(2, "Bob", "bob@example.com", "hash2");
        List<User> mockUsers = Arrays.asList(alice, bob);

        TypedQuery<User> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT u FROM User u ORDER BY u.id", User.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockUsers);

        assertEquals(mockUsers, userService.findAll());
    }

    @Test
    void findById_existingId_shouldReturnUser() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1");
        when(entityManager.find(User.class, 1L)).thenReturn(alice);

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(alice, result.get());
    }

    @Test
    void findById_nonExistingId_shouldReturnEmptyOptional() {
        long id = 99L;
        when(entityManager.find(User.class, id)).thenReturn(null);

        Optional<User> result = userService.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void create_shouldPersistAndReturnUser() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1");

        User result = userService.create(alice);

        verify(entityManager).persist(alice);
        assertEquals(alice, result);
    }

    @Test
    void update_shouldMergeAndReturnUpdatedUser() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1");
        when(entityManager.merge(alice)).thenReturn(alice);

        User result = userService.update(alice);

        verify(entityManager).merge(alice);
        assertEquals(alice, result);
    }

    @Test
    void delete_existingId_shouldRemoveUserAndReturnTrue() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1");
        when(entityManager.find(User.class, alice.getId())).thenReturn(alice);

        boolean result = userService.delete(alice.getId());

        verify(entityManager).remove(alice);
        assertTrue(result);
    }

    @Test
    void delete_nonExistingId_shouldReturnFalse() {
        long id = 99L;
        when(entityManager.find(User.class, id)).thenReturn(null);

        boolean result = userService.delete(id);

        verify(entityManager, never()).remove(any());
        assertFalse(result);
    }

    private User createUser(long id, String userName, String email, String passwordHash) {
        var user = new User();
        user.setId(id);
        user.setUsername(userName);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return user;
    }
}
