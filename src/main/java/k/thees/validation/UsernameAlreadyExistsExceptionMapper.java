package k.thees.validation;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UsernameAlreadyExistsExceptionMapper implements ExceptionMapper<UsernameAlreadyExistsException> {

    @Override
    public Response toResponse(UsernameAlreadyExistsException exception) {
        return Response.status(Response.Status.CONFLICT).entity(exception.getMessage()).build();
    }
}
