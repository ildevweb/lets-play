package com.example.app.repository;

import com.example.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    Optional<User> findById(String id);
}