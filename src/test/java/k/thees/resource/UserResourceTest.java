package k.thees.resource;

import jakarta.ws.rs.core.Response;
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
import java.util.Optional;

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
    void deleteUser_existingId_returnsOk() {
        doNothing().when(userService).delete(1L);

        try (Response response = userResource.deleteUser(1L)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertNull(response.getEntity());
        }
        verify(userService).delete(1L);
    }

    @Test
    void deleteUser_nonExistingId_returnsNotFound() {
        doThrow(new UserNotFoundException(99L)).when(userService).delete(99L);

        try (Response response = userResource.deleteUser(99L)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("User with ID 99 not found", response.getEntity());
        }
        verify(userService).delete(99L);
    }

    @Test
    void deleteUser_notAdmin_returnsForbidden() {
        doThrow(new UserNotAdminException()).when(userService).delete(1L);

        try (Response response = userResource.deleteUser(1L)) {
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
            assertEquals("User not authorized to delete another user", response.getEntity());
        }
        verify(userService).delete(1L);
    }

    @Test
    void updateUser_successfulUpdate_returnsOkWithUpdatedUserDTO() {
        UserDTO userDTO = createUserDTO(1L, "Alice", "alice@example.com", Role.REGULAR_USER_ID);
        User user = UserMapper.toEntity(userDTO);
        User updatedUser = createUser(1L, "Alice", "alice@example.com", "hash1", Role.REGULAR_USER_ID);

        when(userService.update(user)).thenReturn(updatedUser);

        UserDTO responseDTO;
        try (Response response = userResource.updateUser(1L, userDTO)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            responseDTO = (UserDTO) response.getEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(userDTO.id, responseDTO.id);
        assertEquals(userDTO.username, responseDTO.username);
        verify(userService).update(user);
    }

    @Test
    void updateUser_idMismatch_returnsBadRequest() {
        UserDTO userDTO = createUserDTO(2L, "Alice", "alice@example.com", Role.REGULAR_USER_ID);

        try (Response response = userResource.updateUser(1L, userDTO)) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
            assertEquals("ID in path and body must match", response.getEntity());
        }
        verify(userService, never()).update(any());
    }

    @Test
    void updateUser_userNotFound_returnsNotFound() {
        UserDTO userDTO = createUserDTO(1L, "Alice", "alice@example.com", Role.REGULAR_USER_ID);
        User user = UserMapper.toEntity(userDTO);

        when(userService.update(user)).thenThrow(new UserNotFoundException(1L));

        try (Response response = userResource.updateUser(1L, userDTO)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
            assertEquals("User with ID 1 not found", response.getEntity());
        }
        verify(userService).update(user);
    }

    @Test
    void updateUser_userNotAdmin_returnsForbidden() {
        UserDTO userDTO = createUserDTO(1L, "Alice", "alice@example.com", Role.REGULAR_USER_ID);
        User user = UserMapper.toEntity(userDTO);

        when(userService.update(user)).thenThrow(new UserNotAdminException());

        try (Response response = userResource.updateUser(1L, userDTO)) {
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
            assertEquals("User not authorized to update", response.getEntity());
        }
        verify(userService).update(user);
    }

    private UserDTO createUserDTO(Long id, String username, String email, int roleId) {
        UserDTO dto = new UserDTO();
        dto.id = id;
        dto.username = username;
        dto.email = email;
        dto.roleId = roleId;
        return dto;
    }
}