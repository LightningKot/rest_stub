package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponse {
    @JsonProperty("login")
    @NotNull(message = "Login не может быть null")
    @NotBlank(message = "Login не может быть пустым")
    @Size(min = 8, max = 20, message = "Login должен быть от 8 до 20 символов")
    private String login;

    @JsonProperty("status")
    private String status;
}
