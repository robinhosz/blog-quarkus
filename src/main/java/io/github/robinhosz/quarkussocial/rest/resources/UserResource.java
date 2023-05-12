package io.github.robinhosz.quarkussocial.rest.resources;

import io.github.robinhosz.quarkussocial.rest.dto.CreateUserRequest;
import io.github.robinhosz.quarkussocial.rest.entities.User;
import io.github.robinhosz.quarkussocial.rest.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setAge(18);
        user.setName("Robson");

        userRepository.persist(user);

        return Response.ok(user).status(Response.Status.CREATED).build();
    }

    @GET
    public Response findAllUsers() {
        PanacheQuery<User> query = userRepository.findAll();
        return Response.ok(query.list()).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response findAllUsers(@PathParam("id") Long id, CreateUserRequest userRequest) {
        Optional<User> user = userRepository.findByIdOptional(id);//ATALHO CTRL + ALT + V

        if(user.isPresent()) {
            user.get().setName(userRequest.getName());
            user.get().setAge(userRequest.getAge());
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        Optional<User> user = userRepository.findByIdOptional(id);//ATALHO CTRL + ALT + V

        if(user.isPresent()) {
            userRepository.delete(user.get());
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
