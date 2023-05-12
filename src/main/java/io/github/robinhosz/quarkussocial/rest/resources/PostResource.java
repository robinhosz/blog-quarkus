package io.github.robinhosz.quarkussocial.rest.resources;

import io.github.robinhosz.quarkussocial.rest.dto.CreatePostRequest;
import io.github.robinhosz.quarkussocial.rest.dto.PostResponse;
import io.github.robinhosz.quarkussocial.rest.entities.Post;
import io.github.robinhosz.quarkussocial.rest.entities.User;
import io.github.robinhosz.quarkussocial.rest.repository.PostRepository;
import io.github.robinhosz.quarkussocial.rest.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    private UserRepository userRepository;

    @Inject private PostRepository postRepository;

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        Optional<User> user = userRepository.findByIdOptional(userId);

        if(!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();

        post.setText(postRequest.getText());
        post.setUser(user.get());

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response findAllPosts(@PathParam("userId") Long userId) {
        Optional<User> user = userRepository.findByIdOptional(userId);

        if(!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user.get());

        var list = query.list();

        var postResponseList = list.stream()
              //  map(post -> PostResponse.fromEntity(post))
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
