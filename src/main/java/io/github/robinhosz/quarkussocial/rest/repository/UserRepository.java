package io.github.robinhosz.quarkussocial.rest.repository;

import io.github.robinhosz.quarkussocial.rest.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped //cria uma instancia para injeção de dependencia
public class UserRepository implements PanacheRepository<User> {
}
