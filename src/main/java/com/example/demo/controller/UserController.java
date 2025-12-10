package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // API 1: POST - Generate User
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Simple logic to ensure roles exist before saving user
        Set<Role> rolesWithIds = user.getRoles();
        if (rolesWithIds != null) {
            for (Role role : rolesWithIds) {
                // Find or create a role by name
                Role existingRole = roleRepository.findByName(role.getName());
                if (existingRole == null) {
                    existingRole = roleRepository.save(role);
                }
                role.setId(existingRole.getId());
            }
        }
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // API 2: GET - Get User List
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
