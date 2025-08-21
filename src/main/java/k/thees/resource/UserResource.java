package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.UserDTO;
import k.thees.entity.User;
import k.thees.mapper.UserMapper;
import k.thees.security.UserNotAdminException;
import k.thees.security.UserNotFoundException;
import k.thees.service.UserService;

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
        return userService.findById(id)
                          .map(UserMapper::toDTO)
                          .map(Response::ok)
                          .orElse(Response.status(Response.Status.NOT_FOUND))
                          .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            userService.delete(id);
            return Response.ok().build();
        } catch (UserNotFoundException exception) {
            return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
        } catch (UserNotAdminException exception) {
            return Response.status(Response.Status.FORBIDDEN).entity("User not authorized to delete another user").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UserDTO userDTO) {
        if (!id.equals(userDTO.id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID in path and body must match").build();
        }
        User userToUpdate = UserMapper.toEntity(userDTO);
        try {
            User updatedUser = userService.update(userToUpdate);
            return Response.ok(UserMapper.toDTO(updatedUser)).build();
        } catch (UserNotFoundException exception) {
            return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
        } catch (UserNotAdminException exception) {
            return Response.status(Response.Status.FORBIDDEN).entity("User not authorized to update").build();
        }
    }
}