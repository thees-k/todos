package k.thees.service;

import jakarta.persistence.EntityManager;
import k.thees.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static k.thees.testutil.TestDataFactory.createUser;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @Test
    void create_shouldUpdateUpdatedAtToCurrentOrLater() {
        User alice = createUser(1, "Alice", "alice@example.com", "hash1");

        LocalDateTime beforeCreate = LocalDateTime.now();
        User createdUser = userService.create(alice);
        LocalDateTime afterCreate = LocalDateTime.now();

        assertNotNull(createdUser.getUpdatedAt());
        assertFalse(createdUser.getUpdatedAt().isBefore(beforeCreate));
        assertFalse(createdUser.getUpdatedAt().isAfter(afterCreate));
    }
}
