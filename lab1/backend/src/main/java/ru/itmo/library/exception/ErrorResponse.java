package ru.itmo.library.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private final int status;

    private final String reason;

    public ErrorResponse(HttpStatus status, String reason) {
        this.status = status.value();
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
