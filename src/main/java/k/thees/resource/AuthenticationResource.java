package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.AuthenticationDTO;
import k.thees.entity.User;
import k.thees.service.UserService;
import k.thees.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    public Response login(@Valid AuthenticationDTO auth) {
        return userService
                .findByUsername(auth.username)
                .filter(u -> BCrypt.checkpw(auth.password, u.getPasswordHash()))
                .map(User::getUsername)
                .map(JwtUtil::generateToken)
                .map(TokenDTO::new)
                .map(tokenDTO -> Response.ok(tokenDTO).build())
                .orElseGet(() -> Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid credentials")
                        .build());
    }

    public record TokenDTO(String token) {
    }
}
