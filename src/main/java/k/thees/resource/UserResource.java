package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.UpdateUserDTO;
import k.thees.dto.UserDTO;
import k.thees.entity.Role;
import k.thees.entity.User;
import k.thees.mapper.UserMapper;
import k.thees.security.UserNotAdminException;
import k.thees.security.UserNotFoundException;
import k.thees.security.UsernameAlreadyExistsException;
import k.thees.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public Response getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> dtoList = users.stream()
                                     .map(UserMapper::toDTO)
                                     .toList();
        return Response.ok(dtoList).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        User user = userService.findByIdOrThrow(id);
        return Response.ok(UserMapper.toDTO(user)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UpdateUserDTO updateUserDTO) {

        String passwordHash = BCrypt.hashpw(updateUserDTO.password, BCrypt.gensalt());

        User userToUpdate = new User();
        userToUpdate.setId(id);
        userToUpdate.setUsername(updateUserDTO.username);
        userToUpdate.setPasswordHash(passwordHash);
        userToUpdate.setEmail(updateUserDTO.email);
        userToUpdate.setRole(new Role(updateUserDTO.roleId));

        try {
            User updatedUser = userService.update(userToUpdate);
            return Response.ok(UserMapper.toDTO(updatedUser)).build();
        } catch (UserNotFoundException exception) {
            return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
        } catch (UsernameAlreadyExistsException exception) {
            return Response.status(Response.Status.CONFLICT).entity(exception.getMessage()).build();
        } catch (UserNotAdminException exception) {
            return Response.status(Response.Status.FORBIDDEN).entity("User not authorized to update").build();
        }
    }
}