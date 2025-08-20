package k.thees.service;

import jakarta.persistence.EntityManager;
import k.thees.entity.Role;
import k.thees.entity.User;
import k.thees.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static k.thees.testutil.TestDataFactory.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;


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
}