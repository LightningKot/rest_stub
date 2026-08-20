package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/restapi/stub-v1")  // Базовый путь для всех методов
public class StubRestController {

    /*
     GET-метод: возвращает статичный JSON
     Пример запроса: GET http://localhost:8080/restapi/stub-v1/status
     */
    @GetMapping(value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("login", "Login1");
        response.put("status", "ok");

        return ResponseEntity.ok(response);
    }

    /*
     POST-метод: принимает логин/пароль, возвращает их же + дату
     Пример запроса: POST http://localhost:8080/restapi/stub-v1/login
     Тело: {"login":"user123", "password":"qwerty"}
     */
    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResp> postLogin(@RequestBody LoginReq request) {
        System.out.println("Получен запрос: " + request);
        LoginResp response = new LoginResp(request.getLogin(), request.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}