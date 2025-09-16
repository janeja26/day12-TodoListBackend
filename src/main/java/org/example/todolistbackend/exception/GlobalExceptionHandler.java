package org.example.todolistbackend.exception;




import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ResponseEntity<Map<String, Object>> handleValidation(Exception ex){
        Map<String, Object> body = new HashMap<>();
        body.put("code", "VALIDATION_ERROR");
        body.put("message", "Validation failed");

        if (ex instanceof MethodArgumentNotValidException manv) {
            List<Map<String, String>> errors = manv.getBindingResult().getFieldErrors().stream()
                    .map(e -> {
                        Map<String, String> m = new HashMap<>();
                        m.put("field", e.getField());
                        m.put("message", e.getDefaultMessage());
                        return m;
                    }).toList();
            body.put("errors", errors);
        }

        return ResponseEntity.unprocessableEntity().body(body); // 422
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(Exception ex){
        Map<String, Object> body = new HashMap<>();
        body.put("code", "INTERNAL_ERROR");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
