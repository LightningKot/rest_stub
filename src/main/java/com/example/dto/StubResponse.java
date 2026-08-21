package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String login;

    @JsonProperty("password")
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
