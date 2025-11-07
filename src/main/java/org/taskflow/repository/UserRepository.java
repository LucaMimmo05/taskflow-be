package org.taskflow.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.taskflow.dto.UserRequest;
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

    public void updateNotifySetting(User userUpdated) {
        if (userUpdated != null) {
            persistOrUpdate(userUpdated);
        }
    }

    public User update(User user, UserRequest request) {
        if (request == null || user == null) {
            return user;
        }

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            user.setEmail(request.getEmail().trim());
        }
        if (request.getDisplayName() != null && !request.getDisplayName().trim().isEmpty()) {
            user.setDisplayName(request.getDisplayName().trim());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(request.getPassword());
        }

        persistOrUpdate(user);
        return user;
    }
}
