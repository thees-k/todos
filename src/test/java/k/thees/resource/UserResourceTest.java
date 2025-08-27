package k.thees.resource;

import jakarta.ws.rs.core.Response;
import k.thees.dto.UpdateUserDTO;
import k.thees.dto.UserDTO;
import k.thees.entity.Role;
import k.thees.entity.User;
import k.thees.mapper.UserMapper;
import k.thees.security.UserNotAdminException;
import k.thees.security.UserNotFoundException;
import k.thees.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static k.thees.testutil.TestDataFactory.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @InjectMocks
    private UserResource userResource;

    @Mock
    private UserService userService;

    @Test
    void getAllUsers_returnsUserDTOList() {
        User user1 = createUser(1L, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);
        User user2 = createUser(2L, "Bob", "bob@example.com", "hash1", Role.REGULAR_USER_ID);
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
        User user = createUser(1L, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);
        UserDTO expectedDTO = UserMapper.toDTO(user);
        when(userService.findByIdOrThrow(1L)).thenReturn(user);

        Response response = userResource.getUser(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserDTO dto = (UserDTO) response.getEntity();
        assertEquals(expectedDTO, dto);
        verify(userService).findByIdOrThrow(1L);
    }

    @Test
    void deleteUser_existingId_returnsOk() {
        doNothing().when(userService).delete(1L);

        try (Response response = userResource.deleteUser(1L)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertNull(response.getEntity());
        }
        verify(userService).delete(1L);
    }

    @Test
    void updateUser_successfulUpdate_returnsOkWithUpdatedUserDTO() {
        UpdateUserDTO userDTO = createUserDTO("Alice", "Alice", "alice@example.com", Role.REGULAR_USER_ID);
        User updatedUser = createUser(1L, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);

        when(userService.update(any(User.class))).thenReturn(updatedUser);

        UserDTO responseDTO;
        try (Response response = userResource.updateUser(1L, userDTO)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            responseDTO = (UserDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(userDTO.username, responseDTO.username);
        verify(userService).update(any(User.class));
    }

    @Test
    void updateUser_userNotFound_returnsNotFound() {
        UpdateUserDTO userDTO = createUserDTO("Alice", "Alice", "alice@example.com", Role.REGULAR_USER_ID);

        when(userService.update(any(User.class))).thenThrow(new UserNotFoundException(1L));

        try (Response response = userResource.updateUser(1L, userDTO)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("User with ID 1 not found", response.getEntity());
        }
        verify(userService).update(any(User.class));
    }

    @Test
    void updateUser_userNotAdmin_returnsForbidden() {
        UpdateUserDTO userDTO = createUserDTO("Alice", "Alice", "alice@example.com", Role.REGULAR_USER_ID);

        when(userService.update(any(User.class))).thenThrow(new UserNotAdminException());

        try (Response response = userResource.updateUser(1L, userDTO)) {
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
            assertEquals("User not authorized to update", response.getEntity());
        }
        verify(userService).update(any(User.class));
    }

    private UpdateUserDTO createUserDTO(String username, String password, String email, int roleId) {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.username = username;
        dto.password = password;
        dto.email = email;
        dto.roleId = roleId;
        return dto;
    }
}