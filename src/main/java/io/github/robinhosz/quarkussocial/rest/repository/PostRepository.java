package io.github.robinhosz.quarkussocial.rest.repository;

import io.github.robinhosz.quarkussocial.rest.entities.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
