package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    @Transient
    private String passwordConfirmation;
    private boolean enabled;
    private Set<Role> roles;

}
