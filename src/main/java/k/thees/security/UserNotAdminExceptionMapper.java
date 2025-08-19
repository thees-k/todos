package k.thees.security;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserNotAdminExceptionMapper implements ExceptionMapper<UserNotAdminException> {

    @Override
    public Response toResponse(UserNotAdminException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
