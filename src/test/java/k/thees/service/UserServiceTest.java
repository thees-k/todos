package k.thees.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import k.thees.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        userService = new UserService();
        entityManager = mock(EntityManager.class);
        Field emField = UserService.class.getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(userService, entityManager);
    }

    @Test
    void findAll_returnsListOfUsers() {
        TypedQuery<User> query = mock(TypedQuery.class);
        List<User> users = List.of(new User(), new User());

        when(entityManager.createQuery("SELECT u FROM User u", User.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(users, result);
        verify(entityManager).createQuery("SELECT u FROM User u", User.class);
        verify(query).getResultList();
    }

    @Test
    void findById_existingUser_returnsUser() {
        User user = new User();
        user.setId(1L);

        when(entityManager.find(User.class, 1L)).thenReturn(user);

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(entityManager).find(User.class, 1L);
    }

    @Test
    void findById_nonExistingUser_returnsEmpty() {
        when(entityManager.find(User.class, 2L)).thenReturn(null);

        Optional<User> result = userService.findById(2L);

        assertTrue(result.isEmpty());
        verify(entityManager).find(User.class, 2L);
    }

    @Test
    void create_persistsUserAndReturnsIt() {
        User user = new User();

        doNothing().when(entityManager).persist(user);

        User result = userService.create(user);

        assertEquals(user, result);
        verify(entityManager).persist(user);
    }

    @Test
    void update_mergesUserAndReturnsMerged() {
        User user = new User();
        User mergedUser = new User();

        when(entityManager.merge(user)).thenReturn(mergedUser);

        User result = userService.update(user);

        assertEquals(mergedUser, result);
        verify(entityManager).merge(user);
    }

    @Test
    void delete_existingUser_removesUserAndReturnsTrue() {
        User user = new User();

        when(entityManager.find(User.class, 1L)).thenReturn(user);
        doNothing().when(entityManager).remove(user);

        boolean result = userService.delete(1L);

        assertTrue(result);
        verify(entityManager).find(User.class, 1L);
        verify(entityManager).remove(user);
    }

    @Test
    void delete_nonExistingUser_returnsFalse() {
        when(entityManager.find(User.class, 2L)).thenReturn(null);

        boolean result = userService.delete(2L);

        assertFalse(result);
        verify(entityManager).find(User.class, 2L);
        verify(entityManager, never()).remove(any());
    }
}