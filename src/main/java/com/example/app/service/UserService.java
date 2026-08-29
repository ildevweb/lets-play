package com.example.app.service;

import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.dto.UserServiceResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserServiceResponse getUser(String id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return new UserServiceResponse(
                404, 
                "user not found",
                null);
        }

        return new UserServiceResponse(
            200, 
            "User Found",
            user.get());
    }
}
