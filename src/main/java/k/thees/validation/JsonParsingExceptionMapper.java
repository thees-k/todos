package k.thees.validation;

import jakarta.json.bind.JsonbException;
import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Provider
public class JsonParsingExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        Throwable cause = ExceptionUtils.getRootCause(exception);

        if (cause instanceof JsonParsingException || cause instanceof JsonbException || cause instanceof BadRequestException) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Malformed JSON request")
                           .build();
        } else {
            cause.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Internal server error")
                           .build();
        }
    }
}
