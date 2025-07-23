package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.TaskDTO;
import k.thees.entity.Task;
import k.thees.mapper.TaskMapper;
import k.thees.service.TaskService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    private TaskService taskService;

    @GET
    public Response getAllTasks() {
        List<Task> tasks = taskService.findAll();
        List<TaskDTO> dtos = tasks.stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getTask(@PathParam("id") Long id) {
        return taskService.findById(id)
                .map(TaskMapper::toDTO)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    public Response createTask(TaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        Task created = taskService.create(task);
        TaskDTO createdDTO = TaskMapper.toDTO(created);
        return Response.created(URI.create("/api/tasks/" + createdDTO.id)).entity(createdDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long id, TaskDTO taskDTO) {
        taskDTO.id = id; // set id from path param
        Task task = TaskMapper.toEntity(taskDTO);
        Task updated = taskService.update(task);
        TaskDTO updatedDTO = TaskMapper.toDTO(updated);
        return Response.ok(updatedDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        boolean deleted = taskService.delete(id);
        return deleted ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}