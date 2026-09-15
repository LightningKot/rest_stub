package com.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class FileWorker {
    private final String path = "data_output.txt";
    private final ObjectMapper objectMapper;
    private List<String> cachedPrepFile = Collections.emptyList();
    private final Random random = new Random();

    FileWorker(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadFile() {
        String prep_path = "fwith-ten-pre‑generated.txt";
        Path filePath = Path.of(prep_path);
        if (!Files.exists(filePath)) {
            log.warn("File {} not found, cache will be empty", path);
            return;
        }
        try {
            cachedPrepFile = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            System.out.printf("[+] Loaded %d lines from %s\n", cachedPrepFile.size(), prep_path);
            log.info("[logger] Loaded {} lines from {}", cachedPrepFile.size(), prep_path);
        } catch (IOException e) {
            log.error("Failed to read file: {}", prep_path, e);
            cachedPrepFile = Collections.emptyList();
        }
    }

    public void appendJsonLine(Object object) {
        try {
            Path path_ = Path.of(path);
            //Path parent = path_.getParent();
            //if (parent != null) {
            //    Files.createDirectories(parent);
            //}
            String json = objectMapper.writeValueAsString(object);
            Files.writeString(path_, json + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e){
            log.error("Failed to write to file: {}", path, e);
            return;
        }
    }

    public String getRandomLine() {
        //Random random = new Random();
        //try{
        //    List<String> lines = Files.readAllLines(Path.of(path)); //2й файл заранеее сгенерированный 10 строк и его нужно идин раз прочитать и потом от 0 до 9 генерируем случайную строку
        //    log.debug("debug: "+lines.toString());
        //    if (lines.isEmpty()) {
        //        return "No data";
        //    }
        //    return lines.get(random.nextInt(lines.size()));
        //} catch (IOException e) {
        //    log.error("Failed to write to file: {}", path, e);
        //    return "";
        //}
        int idx = random.nextInt(cachedPrepFile.size());
        return  cachedPrepFile.get(idx);
    }
}
