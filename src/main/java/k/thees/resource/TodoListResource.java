package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.SaveTodoListDTO;
import k.thees.dto.TodoListDTO;
import k.thees.entity.TodoList;
import k.thees.mapper.TodoListMapper;
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
    public Response getAllTodoLists() {
        List<TodoList> todoLists = todoListService.findAll();
        List<TodoListDTO> dtos = todoLists.stream()
                                          .map(TodoListMapper::toDTO)
                                          .toList();
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getTodoList(@PathParam("id") Long id) {
        TodoList todoList = todoListService.findByIdOrThrow(id);
        return Response.ok().entity(TodoListMapper.toDTO(todoList)).build();
    }

    @POST
    public Response createTodoList(SaveTodoListDTO todoListDTO) {
        TodoList todoList = TodoListMapper.toEntity(todoListDTO);
        TodoList created = todoListService.create(todoList);
        TodoListDTO createdDTO = TodoListMapper.toDTO(created);
        return Response.created(URI.create("/api/todolists/" + createdDTO.id)).entity(createdDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTodoList(@PathParam("id") Long id, SaveTodoListDTO dto) {
        TodoList todoList = TodoListMapper.toEntity(dto);
        todoList.setId(id);
        TodoList updated = todoListService.update(todoList);
        TodoListDTO updatedDTO = TodoListMapper.toDTO(updated);
        return Response.ok(updatedDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodoList(@PathParam("id") Long id) {
        todoListService.delete(id);
        return Response.noContent().build();
    }
}