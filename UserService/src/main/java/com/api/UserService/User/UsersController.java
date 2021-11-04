package com.api.UserService.User;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping(path="api/user")
@RestController
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @RequestMapping(value="/validateApiKey", method = RequestMethod.GET)
    public User validateApiKey(
        @RequestParam(required = true, name = "API_Key") String API_KEY
    ) {
        return userService.authenticate(API_KEY);
    }

    @PostMapping
    public void registerUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") UUID id) {
        userService.deleteUser(id);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(
        @PathVariable("userId") UUID id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String email
    ) {
        userService.updateUser(id, name, email);
    }

}
