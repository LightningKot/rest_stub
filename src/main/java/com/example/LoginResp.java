package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginResp {
    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    @JsonProperty("date")
    private String date;

    public LoginResp() {
    }

    public LoginResp(String login, String password) {
        this.login = login;
        this.password = password;

        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}