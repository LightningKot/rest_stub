package com.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;

@Service
public class FileWorker {

    //Path path = Path.of("/tmp/data.txt");
    private final String path = "data_output.txt";
    private static final Logger log = LoggerFactory.getLogger(FileWorker.class);

    private final ObjectMapper objectMapper;



    FileWorker(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public void write() {
        try {
            //Files.createFile( Path.of(path));
            Files.writeString(Path.of(path), "more\n",
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);

        }
        catch (IOException e) {
            //System.out.println("IOException: " + e.getMessage());
            log.error("Failed to write to file: {}", path, e);
            // файл уже есть — ничего не делаем
        }
    }

    public void appendJsonLine(Object object) {
        try {
            Path path_ = Path.of(path);
            Path parent = path_.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            String json = objectMapper.writeValueAsString(object);   // без INDENT_OUTPUT!
            Files.writeString(path_, json + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch (IOException e){
            log.error("Failed to write to file: {}", path, e);
        }
    }

    public String getRandomLine() {
        Random random = new Random();
        try{
            List<String> lines = Files.readAllLines(Path.of(path));
            log.debug("debug: "+lines.toString());
            if (lines.isEmpty()) {
                return "No data";
            }
            return lines.get(random.nextInt(lines.size()));
        } catch (IOException e) {
            log.error("Failed to write to file: {}", path, e);
            return "";
        }
    }
}
