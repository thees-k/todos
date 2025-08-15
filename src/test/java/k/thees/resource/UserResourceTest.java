package k.thees.resource;

import jakarta.ws.rs.core.Response;
import k.thees.dto.UserDTO;
import k.thees.entity.User;
import k.thees.mapper.UserMapper;
import k.thees.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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