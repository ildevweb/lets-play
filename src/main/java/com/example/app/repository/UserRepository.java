package com.example.app.repository;

import com.example.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}