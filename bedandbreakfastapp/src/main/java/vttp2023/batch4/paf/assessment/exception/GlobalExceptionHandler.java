package vttp2023.batch4.paf.assessment.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BookingException.class)
    public ResponseEntity<String> handleBookingError(BookingException ex){
        return ResponseEntity.status(500).body(ex.getMessage());
    } 
}
