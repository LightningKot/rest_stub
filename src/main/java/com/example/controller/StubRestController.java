package com.example.controller;

import com.example.dto.StubResponse;
import com.example.service.StubDelay;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restapi/stub-v1")  // Базовый путь для всех методов
@RequiredArgsConstructor
@Validated
public class StubRestController {

    //@Value("${stub.delay.enabled:true}")
    //private boolean delayEnabled;
    private final StubDelay delay;

    /*
     GET-метод: возвращает статичный JSON
     Пример запроса: GET http://localhost:8080/restapi/stub-v1/status
     */
    @GetMapping(value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StubResponse> getStatus()  {

        StubResponse response = StubResponse.withStatus("Login1", "ok");
        delay.sleep_ms();

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    /*
     POST-метод: принимает логин/пароль, возвращает их же + дату
     Пример запроса: POST http://localhost:8080/restapi/stub-v1/login
     Тело: {"login":"user123", "password":"qwerty"}
     */
    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StubResponse> postLogin(@Valid @RequestBody StubResponse request) {
        //System.out.println("Получен запрос: " + request);
        StubResponse response = StubResponse.withDate(request.getLogin(), request.getPass());
        delay.sleep_ms();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}