package com.testTasks.testAssignment.util;

import com.testTasks.testAssignment.exception.UserValidationException;
import com.testTasks.testAssignment.model.UserRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;

public class UserUtils {

    public static <T> ResponseEntity<T> getResponseEntity(T response) {
        HttpStatus status = response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    public static boolean isValidBirthday(LocalDate birthday, int ageThreshold) {
        return birthday.isBefore(LocalDate.now().minusYears(ageThreshold));
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    public static boolean isValidDateFormat(String birthday) {
        try {
            LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void cleanUpdates(Map<String, Object> updates) {
        Set<String> keysToCheck = Set.of("first_name", "last_name", "email", "birthday");

        keysToCheck.forEach(key -> {
            if (updates.containsKey(key)) {
                Object value = updates.get(key);
                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    updates.remove(key);
                }
            }
        });
        updates.remove("id");
    }

    public static void requiredFieldValidation(UserRequestDto requestDto) {
        if (isNull(requestDto.getFirstName())) {
            throw new UserValidationException("The parameter 'first_name' is required");
        }
        if (isNull(requestDto.getLastName())) {
            throw new UserValidationException("The parameter 'last_name' is required");
        }
        if (isNull(requestDto.getEmail())) {
            throw new UserValidationException("The parameter 'email' is required");
        }
        if (isNull(requestDto.getBirthday())) {
            throw new UserValidationException("The parameter 'birthday' is required");
        }
    }
}
