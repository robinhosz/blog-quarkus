package io.github.robinhosz.quarkussocial.rest.resources;

import io.github.robinhosz.quarkussocial.rest.domain.model.User;
import io.github.robinhosz.quarkussocial.rest.dto.CreateUserRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = User.builder()
                .age(userRequest.getAge())
                .name(userRequest.getName())
                .build();
        user.persist();

        return Response.ok(user).status(Response.Status.CREATED).build();
    }

    @GET
    public Response findAllUsers() {

        return Response.ok().build();
    }
}
