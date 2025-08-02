package k.thees.resource;

import jakarta.ws.rs.core.Response;
import k.thees.dto.UserDTO;
import k.thees.entity.User;
import k.thees.mapper.UserMapper;
import k.thees.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @InjectMocks
    private UserResource userResource;

    @Mock
    private UserService userService;

    @Test
    void getAllUsers_returnsUserDTOList() {
        User user1 = createUser(1L, "Alice", "alice@example.com");
        User user2 = createUser(2L, "Bob", "bob@example.com");
        when(userService.findAll()).thenReturn(List.of(user1, user2));
        List<UserDTO> expectedUsers = List.of(UserMapper.toDTO(user1), UserMapper.toDTO(user2));

        Response response = userResource.getAllUsers();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<UserDTO> result = (List<UserDTO>) response.getEntity();
        assertEquals(expectedUsers, result);
        verify(userService).findAll();
    }

    @Test
    void getUser_existingId_returnsOkResponseWithUserDTO() {
        User user = createUser(1L, "Alice", "alice@example.com");
        UserDTO expectedDTO = UserMapper.toDTO(user);
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        Response response = userResource.getUser(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserDTO dto = (UserDTO) response.getEntity();
        assertEquals(expectedDTO, dto);
        verify(userService).findById(1L);
    }

    @Test
    void getUser_nonExistingId_returnsNotFound() {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        Response response = userResource.getUser(99L);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(userService).findById(99L);
    }

    @Test
    void createUser_shouldReturnCreatedResponseWithLocationAndEntity() {
        UserDTO inputDTO = new UserDTO();
        inputDTO.username = "Alice";
        inputDTO.email = "alice@example.com";
        UserMapper.toEntity(inputDTO);

        User createdUser = createUser(1L, "Alice", "alice@example.com");
        UserDTO expectedDTO = UserMapper.toDTO(createdUser);
        when(userService.create(any(User.class))).thenReturn(createdUser);

        UserDTO returnedDTO;
        try (Response response = userResource.createUser(inputDTO)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(URI.create("/api/users/1"), response.getLocation());
            returnedDTO = (UserDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedDTO, returnedDTO);
        verify(userService).create(any(User.class));
    }

@Test
void createUser_withoutId_shouldCreateUserAndReturnCreatedResponse() {
    // Create a new UserDTO without setting an ID (simulating client input without ID)
    UserDTO inputDTO = new UserDTO();
    inputDTO.username = "NewUser";
    inputDTO.email = "newuser@example.com";
    inputDTO.passwordHash = "hash";

    // Create a User entity representing the created user with an assigned ID (e.g., from DB)
    User createdUser = createUser(10L, "NewUser", "newuser@example.com");
    createdUser.setPasswordHash("hash");
    // Convert the created User entity to a DTO for expected response comparison
    UserDTO expectedDTO = UserMapper.toDTO(createdUser);

    // Mock the userService.create() method to return the createdUser when called with any User
    when(userService.create(any(User.class))).thenReturn(createdUser);

    UserDTO returnedDTO;
    // Call the resource method to create the user and capture the response
    try (Response response = userResource.createUser(inputDTO)) {
        // Assert that the response status is 201 Created
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        // Assert that the Location header points to the new user's URI
        assertEquals(URI.create("/api/users/10"), response.getLocation());
        // Extract the returned UserDTO entity from the response
        returnedDTO = (UserDTO) response.getEntity();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    // Assert that the returned DTO matches the expected DTO (with assigned ID)
    assertEquals(expectedDTO, returnedDTO);

    // Capture the User entity passed to userService.create() to verify input
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userService).create(captor.capture());
    // Assert that the User entity passed to create() has no ID set (null)
    assertNull(captor.getValue().getId());
}
    @Test
    void updateUser_shouldReturnOkResponseWithUpdatedUserDTO() {
        UserDTO inputDTO = new UserDTO();
        inputDTO.username = "Updated";
        inputDTO.email = "updated@example.com";
        UserMapper.toEntity(inputDTO);

        User updatedUser = createUser(1L, "Updated", "updated@example.com");
        UserDTO expectedDTO = UserMapper.toDTO(updatedUser);
        when(userService.update(any(User.class))).thenReturn(updatedUser);

        UserDTO returnedDTO;
        try (Response response = userResource.updateUser(1L, inputDTO)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            returnedDTO = (UserDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedDTO, returnedDTO);
        verify(userService).update(any(User.class));
    }

    @Test
    void deleteUser_existingId_returnsNoContent() {
        when(userService.delete(1L)).thenReturn(true);

        try (Response response = userResource.deleteUser(1L)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(userService).delete(1L);
    }

    @Test
    void deleteUser_nonExistingId_returnsNotFound() {
        when(userService.delete(99L)).thenReturn(false);

        try (Response response = userResource.deleteUser(99L)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(userService).delete(99L);
    }

    private User createUser(Long id, String username, String email) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }
}