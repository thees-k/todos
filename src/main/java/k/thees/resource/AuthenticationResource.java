package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.AuthenticationDTO;
import k.thees.entity.User;
import k.thees.service.UserService;
import k.thees.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;


@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    public Response login(@Valid AuthenticationDTO auth) {
        return userService.findByUsername(auth.username)
                .filter(user -> BCrypt.checkpw(auth.password, user.getPasswordHash()))
                .map(User::getUsername)
                .map(JwtUtil::generateToken)
                .map(token -> Map.of("token", token))
                .map(entity -> Response.ok().entity(entity).build())
                .orElseGet(() -> Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build());
    }
}
