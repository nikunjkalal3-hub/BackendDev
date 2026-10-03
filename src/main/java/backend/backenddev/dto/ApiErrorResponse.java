package backend.backenddev.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class ApiErrorResponse {

    private final int status;
    private final String message;
    private final Map<String, String> errors;

    public ApiErrorResponse(
            int status,
            String message,
            Map<String, String> errors) {

        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}