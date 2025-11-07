package org.taskflow.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.taskflow.model.User;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public void registerUser(User user) {
        persist(user);
    }

    public User findById(ObjectId id) {
        return find("_id", id).firstResult();
    }

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public void UpdateNotifySetting( User userUpdated) {
        if (userUpdated != null) {
            update(userUpdated);
        }
    }
}

