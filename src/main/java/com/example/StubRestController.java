package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/restapi/stub-v1")  // Базовый путь для всех методов
public class StubRestController {

    @Value("${stub.delay.enabled:true}")
    private boolean delayEnabled;
    /*
     GET-метод: возвращает статичный JSON
     Пример запроса: GET http://localhost:8080/restapi/stub-v1/status
     */
    @GetMapping(value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getStatus() throws InterruptedException {
        Map<String, String> response = new HashMap<>();
        response.put("login", "Login1");
        response.put("status", "ok");

        if (delayEnabled){
            int delay = ThreadLocalRandom.current().nextInt(1000, 2001);
            Thread.sleep(delay);
        }

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
    public ResponseEntity<LoginResp> postLogin(@RequestBody LoginReq request) throws InterruptedException{
        System.out.println("Получен запрос: " + request);
        LoginResp response = new LoginResp(request.getLogin(), request.getPassword());

        if (delayEnabled){
            int delay = ThreadLocalRandom.current().nextInt(1000, 2001);
            Thread.sleep(delay);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}