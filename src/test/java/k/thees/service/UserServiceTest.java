package k.thees.service;

import jakarta.persistence.EntityManager;
import k.thees.entity.Role;
import k.thees.entity.User;
import k.thees.security.SecurityService;
import k.thees.security.UserNotAdminException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static k.thees.testutil.TestDataFactory.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        lenient().when(securityService.getLoggedInAdminUserOrThrow()).thenReturn(createUser(1L, "Admin", "admin@example.com", "hash1", Role.ADMINISTRATOR_ID));
    }

    @Test
    void create_shouldUpdateCreatedAtAndUpdatedAtToCurrentOrLater() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);

        LocalDateTime beforeCreate = LocalDateTime.now();
        User createdUser = userService.create(alice);
        LocalDateTime afterCreate = LocalDateTime.now();

        validateIsBetween(createdUser.getUpdatedAt(), beforeCreate, afterCreate);

        validateIsBetween(createdUser.getCreatedAt(), beforeCreate, afterCreate);
    }

    private void validateIsBetween(LocalDateTime time, LocalDateTime beforeCreate, LocalDateTime afterCreate) {
        assertNotNull(time);
        assertFalse(time.isBefore(beforeCreate));
        assertFalse(time.isAfter(afterCreate));
    }

    @Test
    void create_shouldSetEqualCreatedAtAndUpdatedAt() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);

        User createdUser = userService.create(alice);

        assertEquals(createdUser.getCreatedAt(), createdUser.getUpdatedAt());
    }


    @Test
    void create_shouldSetUpdatedByToLoggedInUser() {
        User alice = createUser(2, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);

        User createdUser = userService.create(alice);

        assertEquals("Admin", createdUser.getUpdatedBy().getUsername());
    }

    @Test
    void create_shouldSetUpdatedByToUserWithRoleAdministrator() {
        User alice = createUser(2, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);

        User createdUser = userService.create(alice);

        assertEquals(Role.ADMINISTRATOR_ID, createdUser.getUpdatedBy().getRole().getId());
    }

    @Test
    void update_shouldThrowIfStoredUserNotFound() {
        User user = createUser(99L, "User99", "user99@example.com", "hash99", Role.REGULAR_USER_ID);
        when(entityManager.find(User.class, user.getId())).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.update(user));
        assertEquals("User with id 99 does not exist", ex.getMessage());
    }

    @Test
    void update_roleChanged_diffUser_currentUserAdmin() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = createUser(2L, "admin", "admin@example.com", "hashAdmin", Role.ADMINISTRATOR_ID);
        User updatedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.ADMINISTRATOR_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);
        when(entityManager.merge(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.update(updatedUser);

        updatedUser.setUpdatedBy(currentUser);
        validateUser(updatedUser, result);
    }

    @Test
    void update_roleNotChanged_diffUser_currentUserAdmin() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = createUser(2L, "admin", "admin@example.com", "hashAdmin", Role.ADMINISTRATOR_ID);
        User updatedUser = createUser(1L, "user1Updated", "user1updated@example.com", "hash1updated", Role.REGULAR_USER_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);
        when(entityManager.merge(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.update(updatedUser);

        updatedUser.setUpdatedBy(currentUser);
        validateUser(updatedUser, result);
    }

    @Test
    void update_roleChanged_sameUser_currentUserAdmin() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.ADMINISTRATOR_ID);
        User currentUser = storedUser;
        User updatedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);
        when(entityManager.merge(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.update(updatedUser);

        updatedUser.setUpdatedBy(currentUser);
        validateUser(updatedUser, result);
    }

    @Test
    void update_roleChanged_diffUser_currentUserNotAdmin_shouldThrow() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = createUser(2L, "user2", "user2@example.com", "hash2", Role.REGULAR_USER_ID);
        User updatedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.ADMINISTRATOR_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);

        assertThrows(UserNotAdminException.class, () -> userService.update(updatedUser));
    }

    @Test
    void update_roleChanged_sameUser_currentUserNotAdmin_shouldThrow() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = storedUser;
        User updatedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.ADMINISTRATOR_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);

        assertThrows(UserNotAdminException.class, () -> userService.update(updatedUser));
    }

    @Test
    void update_roleNotChanged_diffUser_currentUserNotAdmin_shouldThrow() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = createUser(2L, "user2", "user2@example.com", "hash2", Role.REGULAR_USER_ID);
        User updatedUser = createUser(1L, "user1Updated", "user1updated@example.com", "hash1updated", Role.REGULAR_USER_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);

        assertThrows(UserNotAdminException.class, () -> userService.update(updatedUser));
    }

    @Test
    void update_roleNotChanged_sameUser_currentUserAdmin() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = storedUser;
        User updatedUser = createUser(1L, "user1Updated", "user1updated@example.com", "hash1updated", Role.REGULAR_USER_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);
        when(entityManager.merge(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.update(updatedUser);

        updatedUser.setUpdatedBy(currentUser);
        validateUser(updatedUser, result);
    }

    @Test
    void update_roleNotChanged_sameUser_currentUserNotAdmin() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = storedUser;
        User updatedUser = createUser(1L, "user1Updated", "user1updated@example.com", "hash1updated", Role.REGULAR_USER_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);
        when(entityManager.merge(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.update(updatedUser);

        updatedUser.setUpdatedBy(currentUser);
        validateUser(updatedUser, result);
    }

    private void validateUser(User expected, User actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getRole(), actual.getRole());
        assertEquals(expected.getPasswordHash(), actual.getPasswordHash());
        assertNotNull(actual.getUpdatedAt());
        assertEquals(expected.getUpdatedBy(), actual.getUpdatedBy());
    }

    @Test
    void update_shouldSetUpdatedAtBetweenBeforeAndAfterUpdate() {
        User storedUser = createUser(1L, "user1", "user1@example.com", "hash1", Role.REGULAR_USER_ID);
        User currentUser = createUser(2L, "admin", "admin@example.com", "hashAdmin", Role.ADMINISTRATOR_ID);
        User updatedUser = createUser(1L, "user1Updated", "user1updated@example.com", "hash1updated", Role.REGULAR_USER_ID);

        when(entityManager.find(User.class, 1L)).thenReturn(storedUser);
        when(securityService.getLoggedInUserOrThrow()).thenReturn(currentUser);
        when(entityManager.merge(any())).thenAnswer(i -> i.getArgument(0));

        LocalDateTime beforeUpdate = LocalDateTime.now();
        User result = userService.update(updatedUser);
        LocalDateTime afterUpdate = LocalDateTime.now();

        validateIsBetween(result.getUpdatedAt(), beforeUpdate, afterUpdate);
    }
}