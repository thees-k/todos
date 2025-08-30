package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.SaveTaskDTO;
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
        Task task = taskService.findByIdOrThrow(id);
        return Response.ok().entity(TaskMapper.toDTO(task)).build();
    }

    @POST
    public Response createTask(SaveTaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        Task created = taskService.create(task);
        TaskDTO createdDTO = TaskMapper.toDTO(created);
        return Response.created(URI.create("/api/tasks/" + createdDTO.id)).entity(createdDTO).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTask(@PathParam("id") Long id, SaveTaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        task.setId(id);
        Task updated = taskService.update(task);
        TaskDTO updatedDTO = TaskMapper.toDTO(updated);
        return Response.ok(updatedDTO).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        taskService.delete(id);
        return Response.noContent().build();
    }
}