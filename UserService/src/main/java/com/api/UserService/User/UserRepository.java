package com.api.UserService.User;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    
    @Query("SELECT s FROM User as s WHERE s.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT s FROM User as s WHERE s.ApiKey = :ApiKey")
    Optional<User> findByApiKey(String ApiKey);

    Boolean existsByEmail(String email);
    

}
