package com.api.UserService.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Users")
public class User {

    @Id
    private UUID id;
    private String name;
    private String email;
    private String ApiKey;
    private LocalDate createdAt;


    @Transient
    private Integer timeOfUsage;

    public User() {

    }

    public User(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = LocalDate.now();
        this.ApiKey = "key";
    }

    public User(String name, String email) {
        this(UUID.randomUUID(), name, email);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setApiKey(String API_KEY) {
        this.ApiKey = API_KEY;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Integer getTimeOfUsage() {
        return Period.between(createdAt, LocalDate.now()).getDays();
    }

    public String getApiKey() {
        return this.ApiKey;
    }

    @Override
    public String toString() {
        return "User{" + "Id=" + this.id +
                ", name='" + this.name +'\'' +
                ", email='" + this.email + '\'' +
                '}';
    }

}
