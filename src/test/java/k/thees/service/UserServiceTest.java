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
        lenient().when(securityService.getLoggedInAdminUserOrThrow()).thenReturn(createUser(1L, "Admin", "admin@example.com", "hash1", Role.RoleType.ADMIN));
    }

    @Test
    void create_shouldUpdateCreatedAtAndUpdatedAtToCurrentOrLater() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1", Role.RoleType.REGULAR_USER);

        LocalDateTime beforeCreate = LocalDateTime.now();
        User createdUser = userService.create(alice);
        LocalDateTime afterCreate = LocalDateTime.now();

        assertNotNull(createdUser.getUpdatedAt());
        assertFalse(createdUser.getUpdatedAt().isBefore(beforeCreate));
        assertFalse(createdUser.getUpdatedAt().isAfter(afterCreate));

        assertNotNull(createdUser.getCreatedAt());
        assertFalse(createdUser.getCreatedAt().isBefore(beforeCreate));
        assertFalse(createdUser.getCreatedAt().isAfter(afterCreate));
    }

    @Test
    void create_shouldSetEqualCreatedAtAndUpdatedAt() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1", Role.RoleType.REGULAR_USER);

        User createdUser = userService.create(alice);

        assertEquals(createdUser.getCreatedAt(), createdUser.getUpdatedAt());
    }


    @Test
    void create_shouldUpdatedByToLoggedInUser() {
        User alice = createUser(2, "Alice", "alice@example.com", "hash1", Role.RoleType.REGULAR_USER);

        User createdUser = userService.create(alice);

        assertEquals("Admin", createdUser.getUpdatedBy().getUsername());
    }
}