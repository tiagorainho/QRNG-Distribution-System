package com.api.UserService.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User user = new User(
                "Tiago Rainho",
                "tiago.rainho@ua.pt"
            );
            repository.save(user);
        };
    }
}
