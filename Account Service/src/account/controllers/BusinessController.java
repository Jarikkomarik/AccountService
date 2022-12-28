package account.controllers;

import account.dtos.UserValidationExceptionDto;
import account.dtos.entities.UserSalaryRecord;
import account.services.BusinessService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
public class BusinessController {

    private BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }


    @GetMapping(value = "/api/empl/payment{period}")
    public Object getPaymentForPeriod(@AuthenticationPrincipal User user, YearMonth period) {
        if(period == null) {
            return businessService.getRecords(user.getUsername());
        } else {
            return businessService.getRecord(user.getUsername(), period);
        }
    }

    @PostMapping("api/acct/payments")
    public Map<String, String> addRecords(@RequestBody List<@Valid UserSalaryRecord> userSalaryRecordsList) {
        businessService.saveRecords(userSalaryRecordsList);
        return Map.of("status", "Added successfully!");
    }

    @PutMapping("api/acct/payments")
    public Map<String, String> updateRecords(@RequestBody @Valid UserSalaryRecord userSalaryRecord) {
        businessService.updateRecord(userSalaryRecord);
        return Map.of("status", "Updated successfully!");
    }

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
