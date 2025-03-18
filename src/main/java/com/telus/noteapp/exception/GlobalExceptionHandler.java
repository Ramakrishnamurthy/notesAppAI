package com.telus.noteapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle Note Not Found Exception
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<Object> handleNoteNotFoundException(NoteNotFoundException ex, WebRequest request) {
        if (ex == null) {
            LOGGER.error("NoteNotFoundException is null");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (request == null) {
            LOGGER.error("WebRequest is null");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            LOGGER.error("Note not found exception caught", ex);
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Note Not Found");

            if (ex.getMessage() != null) {
                body.put("message", ex.getMessage());
            } else {
                body.put("message", "Unknown error");
            }

            if (request.getDescription(false) != null) {
                body.put("path", request.getDescription(false));
            } else {
                body.put("path", "Unknown path");
            }

            LOGGER.debug("Returning response for note not found exception: {}", body);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("An error occurred while handling NoteNotFoundException", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Handle Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        LOGGER.error("Global exception caught", ex);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");

        if (ex != null) {
            body.put("message", ex.getMessage());
        } else {
            body.put("message", "Unknown error");
        }

        if (request != null) {
            body.put("path", request.getDescription(false));
        } else {
            body.put("path", "Unknown path");
        }

        LOGGER.debug("Returning response for global exception: {}", body);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}