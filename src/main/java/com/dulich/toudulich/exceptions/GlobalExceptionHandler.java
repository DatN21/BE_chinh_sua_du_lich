package com.dulich.toudulich.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý Exception tổng quát

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        // Chỉ lấy thông báo lỗi mà không in ra tên class exception
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(errorResponse);
    }
    // Xử lý BadRequestException (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Bad Request");
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(errorResponse);
    }

    // Xử lý UnauthorizedException (401)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized: " + ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(errorResponse);
    }

    // Xử lý ForbiddenException (403)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbiddenException(ForbiddenException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Forbidden: " + ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(errorResponse);
    }

    // Xử lý NotFoundException (404)
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Not Found: " + ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(errorResponse);
    }

    // Xử lý IllegalArgumentException (ví dụ với thông báo: "Maximum number of images per tour is 10")


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        System.out.println("Exception class: " + ex.getClass().getName());
        errorResponse.put("error", "An unexpected error occurred");
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(errorResponse);
    }

}
