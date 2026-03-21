package com.company.prueba_tecnica.infrastructure.web.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object error;
    private String path;
    private LocalDateTime timestamp;

    public ApiResponse(boolean success, String message, T data, Object error, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data, String message, String path) {
        return new ApiResponse<>(true, message, data, null, path);
    }

    public static <T> ApiResponse<T> error(String message, Object error, String path) {
        return new ApiResponse<>(false, message, null, error, path);
    }
}