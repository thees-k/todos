package k.thees.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import k.thees.dto.RoleDTO;
import k.thees.entity.Role;
import k.thees.mapper.RoleMapper;
import k.thees.service.RoleService;

import java.util.List;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    @Inject
    private RoleService roleService;

    @GET
    public Response getAllRoles() {
        List<Role> roles = roleService.findAll();
        List<RoleDTO> dtoList = roles.stream()
                                     .map(RoleMapper::toDTO)
                                     .toList();
        return Response.ok(dtoList).build();
    }

    @GET
    @Path("/{id}")
    public Response getRole(@PathParam("id") Integer id) {
        return roleService.findById(id)
                          .map(RoleMapper::toDTO)
                          .map(Response::ok)
                          .orElse(Response.status(Response.Status.NOT_FOUND))
                          .build();
    }
}
