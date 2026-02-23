package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.exception.BadRequestExceptionDetails;
import academy.devdojo.springboot2.exception.ValidationExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre){
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .tittle("Bad Request Exception, Check the documentation")
                        .details(bre.getMessage())
                        .developerMessage(bre.getClass().getName()).build(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException exception){
        List<FieldError> fieldsErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldsErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldsErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));


        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .tittle("Bad Request Exception, Invalid fields")
                        .details(exception.getMessage())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .developerMessage(exception.getClass().getName()).build(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex){
        ProblemDetail problemDetail = ProblemDetail
                .forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setTitle("Unexpected Error");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("DeveloperMessage",ex.getClass().getName());
        return problemDetail;
    }
}
