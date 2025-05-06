package com.learn.userservice.service;

import com.learn.userservice.model.Users;
import com.learn.userservice.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    // Create a new user
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    // Get a user by ID
    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }

    // Get all users
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // Update an existing user
    public Users updateUser(Long id, Users updatedUser) {
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
        usersRepository.deleteById(id);
    }
}