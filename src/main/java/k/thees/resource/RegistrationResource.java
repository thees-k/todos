package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.RegistrationDTO;
import k.thees.dto.UserDTO;
import k.thees.entity.User;
import k.thees.mapper.UserMapper;
import k.thees.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URI;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationResource {

    @Inject
    private UserService userService;

    @POST
    public Response registerUser(@Valid RegistrationDTO registrationDTO) {
        // Hash the password
        String passwordHash = BCrypt.hashpw(registrationDTO.password, BCrypt.gensalt());

        // Map to DTO or entity
        UserDTO userDTO = new UserDTO();
        userDTO.username = registrationDTO.username;
        userDTO.email = registrationDTO.email;
        userDTO.passwordHash = passwordHash;

        // Delegate to existing user creation logic
        User created = userService.create(UserMapper.toEntity(userDTO));
        UserDTO createdDTO = UserMapper.toDTO(created);

        return Response.created(URI.create("/api/users/" + createdDTO.id)).entity(createdDTO).build();
    }
}
