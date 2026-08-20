package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginReq {
    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    // Пустой конструктор нужен для десериализации JSON
    public LoginReq() {
    }

    public LoginReq(String login, String password) {
        this.login = login;
        this.password = password;
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

    @Override
    public String toString() {
        return "LoginReq{login='" + login + "', password='" + password + "'}";
    }
}