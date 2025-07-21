package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.entity.TodoList;
import k.thees.service.TodoListService;

import java.net.URI;
import java.util.List;

@Path("/todolists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoListResource {

    @Inject
    private TodoListService todoListService;

    @GET
    public List<TodoList> getAllTodoLists() {
        return todoListService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getTodoList(@PathParam("id") Long id) {
        return todoListService.findById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response createTodoList(TodoList todoList) {
        TodoList created = todoListService.create(todoList);
        return Response.created(URI.create("/api/todolists/" + created.getId())).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTodoList(@PathParam("id") Long id, TodoList todoList) {
        todoList.setId(id);
        TodoList updated = todoListService.update(todoList);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodoList(@PathParam("id") Long id) {
        boolean deleted = todoListService.delete(id);
        return deleted ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}