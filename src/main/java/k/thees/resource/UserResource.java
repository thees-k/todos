package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.UserDTO;
import k.thees.entity.User;
import k.thees.mapper.UserMapper;
import k.thees.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        boolean deleted = userService.delete(id);
        return deleted ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}