package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotBlank(message = "Login is required and cannot be empty")
    //@Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    private String login;

    @NotBlank(message = "email is required and cannot be empty")
    //@Size(min = 10, max = 50, message = "email must be between 10 and 50 characters")
    private String email;

    @NotBlank(message = "password is required and cannot be empty")
    //@Size(min = 10, max = 50, message = "password must be between 10 and 50 characters")
    @JsonProperty("password")
    private String pass;

    @JsonProperty("created_at")
    private String date;

    //public User(String login, String email, String pass) {
    //    this.login = login;
    //    this.email = email;
    //    this.pass = pass;
    //    this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    //}
}
