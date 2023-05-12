package io.github.robinhosz.quarkussocial.rest.resources;

import io.github.robinhosz.quarkussocial.rest.dto.CreateUserRequest;
import io.github.robinhosz.quarkussocial.rest.dto.ResponseError;
import io.github.robinhosz.quarkussocial.rest.entities.User;
import io.github.robinhosz.quarkussocial.rest.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import javax.xml.transform.Source;
import java.util.Optional;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Validator validator;

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {


        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);

        if(!violations.isEmpty()) {
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        userRepository.persist(user);

        return Response.ok(user).status(Response.Status.CREATED).entity(user).build();
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
