package com.testTasks.testAssignment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Set;

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
        Set<String> keysToCheck = Set.of("firstName", "lastName", "email", "birthday");

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
}
