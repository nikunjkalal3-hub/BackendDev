package backend.backenddev.exception;

import backend.backenddev.exception.EmailAlreadyExistsException;
import backend.backenddev.exception.GlobalExceptionHandler;
import backend.backenddev.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void handleUserNotFound_shouldReturn404() {

        UserNotFoundException exception =
                new UserNotFoundException("User not found with id: 1");

        ResponseEntity<Map<String, String>> response =
                handler.handlerUserNotFoundException(exception);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        assertEquals(
                "User not found with id: 1",
                response.getBody().get("message")
        );
    }



    @Test
    void handleEmailAlreadyExists_shouldReturn409() {

        EmailAlreadyExistsException exception =
                new EmailAlreadyExistsException("Email already exists");

        ResponseEntity<Map<String, String>> response =
                handler.handlerEmailAlreadyExists(exception);

        assertEquals(
                HttpStatus.CONFLICT,
                response.getStatusCode()
        );

        assertEquals(
                "Email already exists",
                response.getBody().get("message")
        );
    }
}