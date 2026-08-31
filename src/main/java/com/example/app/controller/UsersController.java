package com.example.app.controller;

import com.example.app.dto.UserServiceResponse;
import com.example.app.model.Product;
import com.example.app.model.User;
import com.example.app.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();

        return ResponseEntity.ok(users);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserServiceResponse> getUser(@PathVariable String id) {
        UserServiceResponse response = userService.getUser(id);

        return ResponseEntity
            .status(response.getStatus())
            .body(response);
    }

    @PostMapping
    public ResponseEntity<UserServiceResponse> postUser(@RequestBody User user) {
        return userService.createUser(user);
    }

}