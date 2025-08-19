package k.thees.resource;

import jakarta.ws.rs.core.Response;
import k.thees.dto.RegistrationDTO;
import k.thees.dto.UserDTO;
import k.thees.entity.Role;
import k.thees.entity.User;
import k.thees.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationResourceTest {

    @InjectMocks
    private RegistrationResource registrationResource;

    @Mock
    private UserService userService;

    @Test
    void registerUser_shouldHashPasswordAndReturnCreatedResponse() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.username = "newuser";
        registrationDTO.email = "newuser@example.com";
        registrationDTO.isAdmin = false;
        registrationDTO.password = "password123";

        // Prepare the User entity that userService.create() will return
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setUsername(registrationDTO.username);
        createdUser.setEmail(registrationDTO.email);
        createdUser.setRole(new Role(Role.RoleType.REGULAR_USER));
        createdUser.setPasswordHash("hashedPassword");

        when(userService.create(any(User.class))).thenReturn(createdUser);

        UserDTO returnedDTO;
        try (Response response = registrationResource.registerUser(registrationDTO)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(URI.create("/api/users/1"), response.getLocation());

            returnedDTO = (UserDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertNotNull(returnedDTO);
        assertEquals(createdUser.getId(), returnedDTO.id);
        assertEquals(createdUser.getUsername(), returnedDTO.username);
        assertEquals(createdUser.getEmail(), returnedDTO.email);
        assertEquals(createdUser.getPasswordHash(), returnedDTO.passwordHash);

        // Capture the User passed to userService.create() to verify password hashing
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).create(captor.capture());
        User passedUser = captor.getValue();

        assertEquals(registrationDTO.username, passedUser.getUsername());
        assertEquals(registrationDTO.email, passedUser.getEmail());
        assertNotNull(passedUser.getPasswordHash());
        assertTrue(BCrypt.checkpw(registrationDTO.password, passedUser.getPasswordHash()));
    }
}