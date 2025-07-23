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

        List<UserDTO> result = userResource.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).username);
        assertEquals("Bob", result.get(1).username);
        verify(userService).findAll();
    }

    @Test
    void getUser_existingId_returnsOkResponseWithUserDTO() {
        User user = createUser(1L, "Alice", "alice@example.com");
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        Response response = userResource.getUser(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserDTO dto = (UserDTO) response.getEntity();
        assertEquals("Alice", dto.username);
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

        User userEntity = UserMapper.toEntity(inputDTO);
        User createdUser = createUser(1L, "Alice", "alice@example.com");
        when(userService.create(any(User.class))).thenReturn(createdUser);

        Response response = userResource.createUser(inputDTO);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(URI.create("/api/users/1"), response.getLocation());
        UserDTO returnedDTO = (UserDTO) response.getEntity();
        assertEquals("Alice", returnedDTO.username);
        verify(userService).create(any(User.class));
    }

    @Test
    void updateUser_shouldReturnOkResponseWithUpdatedUserDTO() {
        UserDTO inputDTO = new UserDTO();
        inputDTO.username = "Updated";
        inputDTO.email = "updated@example.com";

        User userEntity = UserMapper.toEntity(inputDTO);
        User updatedUser = createUser(1L, "Updated", "updated@example.com");
        when(userService.update(any(User.class))).thenReturn(updatedUser);

        Response response = userResource.updateUser(1L, inputDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserDTO returnedDTO = (UserDTO) response.getEntity();
        assertEquals("Updated", returnedDTO.username);
        verify(userService).update(any(User.class));
    }

    @Test
    void deleteUser_existingId_returnsNoContent() {
        when(userService.delete(1L)).thenReturn(true);

        Response response = userResource.deleteUser(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(userService).delete(1L);
    }

    @Test
    void deleteUser_nonExistingId_returnsNotFound() {
        when(userService.delete(99L)).thenReturn(false);

        Response response = userResource.deleteUser(99L);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
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