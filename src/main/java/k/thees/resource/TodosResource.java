package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.model.Todo;
import k.thees.service.TodoService;

import java.util.List;

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodosResource {

    private final TodoService todoService;

    @Inject
    public TodosResource(TodoService todoService) {
        this.todoService = todoService;
    }

    @GET
    public Response getAllTodos() {
        return Response.ok(todoService.getAllTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response getTodoById(@PathParam("id") Long id) {
        // e.g. /api/todos/42
        return Response.ok(todoService.getTodoById(id)).build();
    }

    @POST
    public Response createTodo(Todo todo) {
        return Response.ok(todoService.createTodo(todo)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTodo(@PathParam("id") Long id, Todo todo) {
        // e.g. /api/todos/42
        if(todoService.updateTodo(id, todo)) {
            return Response.ok(todo).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodo(@PathParam("id") Long id) {
        // e.g. /api/todos/42
        if(todoService.deleteTodo(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
