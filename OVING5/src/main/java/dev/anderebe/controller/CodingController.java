package dev.anderebe.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.anderebe.models.CodingResponse;
import dev.anderebe.services.CodingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/coding")
public class CodingController {
    private static final Logger logger = LoggerFactory.getLogger(CodingController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("")
    public String intro() {
        return "Welcome to the coding API!";
    }

    @PostMapping("/run")
    public ResponseEntity<String> run(@RequestBody Map<String, String> input, HttpServletRequest request) {
        try {
            String code = input.get("code");
            String language = input.get("language");
            logger.info("Request received to run code in " + language);
            logger.info("Compiling....");
            String result = CodingService.run(code, language);
            CodingResponse response = new CodingResponse(code, language, result);
            String jsonResponse = objectMapper.writeValueAsString(response);
            logger.info("Returned result of running code in " + language);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            logger.error("Compiling failed! Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
