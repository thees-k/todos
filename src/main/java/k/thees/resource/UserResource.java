package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.entity.User;
import k.thees.service.UserService;

import java.net.URI;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        return userService.findById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response createUser(User user) {
        User created = userService.create(user);
        return Response.created(URI.create("/api/users/" + created.getId())).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        user.setId(id); // assume your entity allows this
        User updated = userService.update(user);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        boolean deleted = userService.delete(id);
        return deleted ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
