package org.taskflow.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.taskflow.model.User;
@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {
}
