package com.example.controller;

import com.example.dao.DataBaseWorker;
import com.example.dto.StatusResponse;
import com.example.dto.StubResponse;
import com.example.model.User;
import com.example.service.StubDelay;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/restapi/stub-v-with-bd")  // Базовый путь для всех методов
@RequiredArgsConstructor
@Validated
public class StubRestController {

    private final StubDelay delay;
    // DataBaseWorker dbw; //работа с бд

    /*
     GET-метод: возвращает статичный JSON
     Пример запроса: GET http://localhost:8080/restapi/stub-v-with-bd/status
     */
    @GetMapping(value = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusResponse> getStatus()  {
        StatusResponse response = new StatusResponse("Login1", "ok");
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

    @GetMapping(value = "/getuser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByLogin(@RequestParam String login)  {
        Optional<User> selectUser = DataBaseWorker.findUserByLogin(login);
        delay.sleep_ms();
        if(selectUser.isPresent()){
            System.out.println(selectUser.get());
            return ResponseEntity.status(HttpStatus.OK).body(selectUser.get());
        }
        else{
            System.out.println("[NOT FOUND] User WITH LOGIN " + login);
            return ResponseEntity.status(HttpStatus.OK).body(selectUser.get());
        }

    }

    @PostMapping(value = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> postLoginIns(@Valid @RequestBody User request) throws SQLException {
        System.out.println("[insert] Получен запрос: " + request);
        //DataBaseWorker.test_connection();
        DataBaseWorker.insertUser(request);
        //StubResponse response = StubResponse.withDate(request.getLogin(), request.getPass());
        delay.sleep_ms();

        return ResponseEntity.status(HttpStatus.OK).body(request);
    }

}