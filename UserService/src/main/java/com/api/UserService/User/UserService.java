package com.api.UserService.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private final UserRepository userRepository;

    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User authenticate(String API_KEY) {
        Optional<User> user = this.userRepository.findByApiKey(API_KEY);
        if(user.isEmpty()) throw new IllegalStateException("API KEY not valid.");
        return user.get();
    }

    public void addNewUser(User user) {
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        if(userByEmail.isPresent()) {
            throw new IllegalStateException("Email Taken");
        }
        user.setId(UUID.randomUUID());
        userRepository.save(
            new User(
                user.getName(),
                user.getEmail()
            )
        );
    }

    @Transactional
    public void updateUser(UUID id, String name, String email) {
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new IllegalStateException("user " + id + " does not exist"));

        if(name != null && name.length() > 0 && !user.getName().equals(name)) user.setName(name);
        if(email != null && email.length() > 0 && !user.getEmail().equals(email)) {
            if(userRepository.existsByEmail(email)) {
                throw new IllegalStateException("Email taken");
            }
            user.setEmail(email);
        }
    }

    public void deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new IllegalStateException("User does not exist");
        }
        userRepository.deleteById(id);
    }

    
}
