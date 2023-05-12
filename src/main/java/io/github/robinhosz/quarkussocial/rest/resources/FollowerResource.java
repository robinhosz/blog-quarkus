package io.github.robinhosz.quarkussocial.rest.resources;

import io.github.robinhosz.quarkussocial.rest.dto.FollowerRequest;
import io.github.robinhosz.quarkussocial.rest.dto.FollowerResponse;
import io.github.robinhosz.quarkussocial.rest.dto.FollowersPerUserResponse;
import io.github.robinhosz.quarkussocial.rest.entities.Follower;
import io.github.robinhosz.quarkussocial.rest.entities.User;
import io.github.robinhosz.quarkussocial.rest.repository.FollowerRepository;
import io.github.robinhosz.quarkussocial.rest.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    @Inject
    private FollowerRepository followerRepository;

    @Inject
    private UserRepository userRepository;

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {

        if(userId.equals(followerRequest.getFollowerId())) {
            return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself! ").build();
        }

        Optional<User> user = userRepository.findByIdOptional(userId);

        if(!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

       var follower = userRepository.findByIdOptional(followerRequest.getFollowerId());

        boolean isFollows = followerRepository.follows(follower.get(), user.get());

        if(!isFollows) {
            var entity = new Follower();
            entity.setFollower(follower.get());
            entity.setUser(user.get());
            followerRepository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {

        Optional<User> user = userRepository.findByIdOptional(userId);

        if(!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = followerRepository.findByUser(userId);

        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(list.size());

        var followerList = list.stream().map(FollowerResponse::new).collect(Collectors.toList());

        responseObject.setContent(followerList);

        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {

        Optional<User> user = userRepository.findByIdOptional(userId);

        if(!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);



        return Response.status(Response.Status.NO_CONTENT).build();

    }

}
