package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.CreateTodoListDTO;
import k.thees.dto.TodoListDTO;
import k.thees.entity.TodoList;
import k.thees.entity.User;
import k.thees.mapper.TodoListMapper;
import k.thees.service.TodoListService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/todolists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoListResource {

    @Inject
    private TodoListService todoListService;

    @GET
    public Response getAllTodoLists() {
        List<TodoList> todoLists = todoListService.findAll();
        List<TodoListDTO> dtos = todoLists.stream()
                                          .map(TodoListMapper::toDTO)
                                          .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getTodoList(@PathParam("id") Long id) {
        return todoListService.findById(id)
                              .map(TodoListMapper::toDTO)
                              .map(Response::ok)
                              .orElse(Response.status(Response.Status.NOT_FOUND))
                              .build();
    }

    @POST
    public Response createTodoList(TodoListDTO todoListDTO) {
        TodoList todoList = TodoListMapper.toEntity(todoListDTO);
        TodoList created = todoListService.create(todoList);
        TodoListDTO createdDTO = TodoListMapper.toDTO(created);
        return Response.created(URI.create("/api/todolists/" + createdDTO.id)).entity(createdDTO).build();
    }

    @POST
    public Response createTodoList(CreateTodoListDTO todoListDTO) {

        TodoList todoList = new TodoList();
        todoList.setName(todoListDTO.name);
        todoList.setDescription(todoListDTO.description);
        todoList.setDone(todoListDTO.isDone);
        todoList.setOwner(new User(todoListDTO.ownerId));
        todoList.setPublic(todoListDTO.isPublic);

        TodoList created = todoListService.create(todoList);
        TodoListDTO createdDTO = TodoListMapper.toDTO(created);
        return Response.created(URI.create("/api/todolists/" + createdDTO.id)).entity(createdDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTodoList(@PathParam("id") Long id, TodoListDTO todoListDTO) {
        todoListDTO.id = id; // set id from path param
        TodoList todoList = TodoListMapper.toEntity(todoListDTO);
        TodoList updated = todoListService.update(todoList);
        TodoListDTO updatedDTO = TodoListMapper.toDTO(updated);
        return Response.ok(updatedDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodoList(@PathParam("id") Long id) {
        boolean deleted = todoListService.delete(id);
        return deleted ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}