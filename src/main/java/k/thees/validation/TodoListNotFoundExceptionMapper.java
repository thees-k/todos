package k.thees.validation;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TodoListNotFoundExceptionMapper implements ExceptionMapper<TodoListNotFoundException> {

    @Override
    public Response toResponse(TodoListNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}