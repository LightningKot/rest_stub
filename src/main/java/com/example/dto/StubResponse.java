package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StubResponse {
    @JsonProperty("login")
    @NotNull(message = "Login не может быть null")
    @NotBlank(message = "Login не может быть пустым")
    @Size(min = 8, max = 20, message = "Login должен быть от 8 до 20 символов")
    private String login;

    @JsonProperty("password")
    @NotNull(message = "password не может быть null")
    @NotBlank(message = "password не может быть пустым")
    @Size(min = 8, max = 20, message = "password должен быть от 8 до 20 символов")
    private String pass;

    @JsonProperty("status")
    private String status;

    @JsonProperty("date")
    private String date;

    public static StubResponse withStatus(String login, String status) {
        return new StubResponse(login, null, status, null);
    }

    public static StubResponse withDate(String login, String password) {
        return new StubResponse(login, password, null, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }


}
