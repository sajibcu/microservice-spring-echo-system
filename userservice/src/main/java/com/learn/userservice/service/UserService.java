package com.learn.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.learn.userservice.model.Users;
import com.learn.userservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UsersRepository usersRepository;

    // Create a new user
    public Users createUser(Users user) {
        log.info("Creating a new user: {}", user);
        return usersRepository.save(user);
    }

    // Get a user by ID
    public Users getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Users getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Get all users
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // Update an existing user
    public Users updateUser(Long id, Users updatedUser) {
        log.info("Updating user with id: {}", id);
        return usersRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            return usersRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        usersRepository.deleteById(id);
    }
}