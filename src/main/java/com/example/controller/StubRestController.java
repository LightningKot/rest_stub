package com.example.controller;

import com.example.dao.DataBaseWorker;
import com.example.dto.ErrorResponse;
import com.example.dto.StatusResponse;
import com.example.dto.StubResponse;
import com.example.exception.DatabaseException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.service.FileWorker;
import com.example.service.StubDelay;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    private final DataBaseWorker dbWorker;
    private final FileWorker fwk;

    //public StubRestController(StubDelay delay, DataBaseWorker dbWorker) {
    //    this.delay = delay;
    //    this.dbWorker = dbWorker;
    //}

    //GET http://localhost:8080/restapi/stub-v-with-bd/getuser
    @GetMapping(value = "/getuser",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByLogin(@RequestParam String login,
                                            HttpServletRequest httpRequest)  {

        delay.sleep_ms();

        try {
            Optional<User> selectUser = dbWorker.findUserByLogin(login); // add try catch
            if (selectUser.isPresent()) {
                System.out.println(selectUser.get());
                return ResponseEntity.status(HttpStatus.OK).body(selectUser.get());
            } else {
                System.out.println("[NOT FOUND] User WITH LOGIN " + login);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "Database Error",
                        "Database operation failed",
                        httpRequest.getRequestURI()
                ));
            }
        } catch (UserNotFoundException e){
            return ResponseEntity.status(400).body(new ErrorResponse(400,
                    "User NOT found",
                    "UserNotFoundException: " + e.getMessage(),
                    httpRequest.getRequestId()));
        }

    }

    // POST http://localhost:8080/restapi/stub-v-with-bd/user
    @PostMapping(value = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postLoginIns(@Valid @RequestBody User request,
                                               HttpServletRequest httpRequest) throws SQLException {
        System.out.println("[insert] Получен запрос: " + request);
        delay.sleep_ms();
        try {
            return ResponseEntity.ok("Response " + dbWorker.insertUser(request));
        } catch (DatabaseException e) {
            //log.error("DB error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(
                            500,
                            "Database Error",
                            "Database operation failed",
                            httpRequest.getRequestURI()
                    )
            );
        }
        catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(400,
                    "Bad Request",
                    "Invalid JSON format: " + e.getMessage(),
                    httpRequest.getRequestId()));
        }
        catch (RuntimeException e) {
            //throw new RuntimeException(e);
            return ResponseEntity.status(400).body(new ErrorResponse(400,
                    "Bad Request",
                    "RuntimeException: " + e.getMessage(),
                    httpRequest.getRequestId()));
        }
    }

    @GetMapping(value = "/getuserfromfile",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByFile(HttpServletRequest httpRequest)  {
        //delay.sleep_ms();

        try {
            String out = fwk.getRandomLine();
            if (!out.isEmpty()) {
                System.out.println(out);
                return ResponseEntity.status(HttpStatus.OK).body(out);
            } else {
                System.out.println("[NOT FOUND]");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "Error",
                        "File operation failed",
                        httpRequest.getRequestURI()
                ));
            }
        } catch (UserNotFoundException e){
            return ResponseEntity.status(400).body(new ErrorResponse(400,
                    "User NOT found",
                    "UserNotFoundException: " + e.getMessage(),
                    httpRequest.getRequestId()));
        }

    }

}