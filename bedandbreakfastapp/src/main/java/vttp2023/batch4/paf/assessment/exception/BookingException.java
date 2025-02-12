package vttp2023.batch4.paf.assessment.exception;

public class BookingException extends RuntimeException{
    public BookingException(){
    }
    public BookingException(String message){
        super(message);
    }
    public BookingException(String message, Throwable throwable){
        super(message, throwable);
    }
}
