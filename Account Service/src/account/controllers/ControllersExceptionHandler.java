package account.controllers;

import account.dtos.UserValidationExceptionDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllersExceptionHandler {


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UserValidationExceptionDto> handleConstraintViolationException(HttpServletRequest request, DataIntegrityViolationException ex) {
        return new ResponseEntity<>(
                new UserValidationExceptionDto(
                        LocalDateTime.now().toString(),
                        BAD_REQUEST.value(),
                        "Bad Request",
                        "User already have record for this period",
                        request.getServletPath()), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<UserValidationExceptionDto> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {

        String errorsMsg = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new UserValidationExceptionDto(
                        LocalDateTime.now().toString(),
                        BAD_REQUEST.value(),
                        "Bad Request",
                        errorsMsg,
                        request.getServletPath()), BAD_REQUEST);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<UserValidationExceptionDto> handleConversionFailedException(HttpServletRequest request, ConversionFailedException ex) {

        return new ResponseEntity<>(
                new UserValidationExceptionDto(
                        LocalDateTime.now().toString(),
                        BAD_REQUEST.value(),
                        "Bad Request",
                        "Incompatible request parameter.",
                        request.getServletPath()), BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<UserValidationExceptionDto> handleNoSuchElementException(HttpServletRequest request, NoSuchElementException ex) {


        return new ResponseEntity<>(
                new UserValidationExceptionDto(
                        LocalDateTime.now().toString(),
                        BAD_REQUEST.value(),
                        "Bad Request",
                        "No records found for your request",
                        request.getServletPath()), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserValidationExceptionDto> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {

        String errorsMsg = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new UserValidationExceptionDto(
                        LocalDateTime.now().toString(),
                        BAD_REQUEST.value(),
                        "Bad Request",
                        errorsMsg,
                        request.getServletPath()), BAD_REQUEST);
    }

}