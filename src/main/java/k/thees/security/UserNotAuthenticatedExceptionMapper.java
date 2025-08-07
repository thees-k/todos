package k.thees.security;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserNotAuthenticatedExceptionMapper implements ExceptionMapper<UserNotAuthenticatedException> {

    @Override
    public Response toResponse(UserNotAuthenticatedException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}