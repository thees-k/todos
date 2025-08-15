package k.thees.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                               .stream()
                               .map(ConstraintViolation::getMessage)
                               .toList();
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(errors)
                       .build();
    }
}
