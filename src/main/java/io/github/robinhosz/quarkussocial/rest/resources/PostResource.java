package io.github.robinhosz.quarkussocial.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    @POST
    public Response savePost() {
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response findAllPosts() {
        return Response.ok().build();
    }
}
