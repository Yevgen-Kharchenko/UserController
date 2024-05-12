package com.testTasks.testAssignment.rest;

import com.testTasks.testAssignment.config.ApplicationProperties;
import com.testTasks.testAssignment.exception.UserValidationException;
import com.testTasks.testAssignment.model.User;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import com.testTasks.testAssignment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.testTasks.testAssignment.util.UserUtils.cleanUpdates;
import static com.testTasks.testAssignment.util.UserUtils.isValidBirthday;
import static com.testTasks.testAssignment.util.UserUtils.isValidDateFormat;
import static com.testTasks.testAssignment.util.UserUtils.isValidEmail;
import static com.testTasks.testAssignment.util.UserUtils.requiredFieldValidation;
import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;
    private final ApplicationProperties applicationProperties;

    @Operation(summary = "Obtaining data about the user by id")
    @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = UserResponseDto.class))})
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return service.findUserById(id);
    }

    @Operation(summary = "Obtaining user data")
    @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = UserResponseDto.class))})
    @Parameters({
            @Parameter(name = "from", description = "Date of birth from which to search for users."),
            @Parameter(name = "to", description = "Date of birth to search for users."),
            @Parameter(name = "limit", description = "Quantity of returned records. Default value = 3"),
            @Parameter(name = "offset", description = "Offset (from which element to return). Default value = 0")
    })
    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false, name = "from") LocalDate from,
            @RequestParam(required = false, name = "to") LocalDate to,
            @RequestParam(required = false, name = "limit", defaultValue = "3") Integer limit,
            @RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset) {
        if (isNull(to)) {
            to = LocalDate.now().minusYears(applicationProperties.appConfig().getAge());
        }
        if (isNull(from)) {
            from = LocalDate.parse(applicationProperties.appConfig().getMinBirthday());
        }
        if (from.isAfter(to)) {
            throw new UserValidationException("Parameter 'from' cannot be after 'to'");
        }
        return service.findAllUsers(from, to, limit, offset);
    }

    @PostMapping("/")
    public ResponseEntity<User> createNew(@Valid @RequestBody UserRequestDto newUser) {
        requiredFieldValidation(newUser);
        if (!isValidDateFormat(newUser.getBirthday().toString())) {
            throw new UserValidationException("Invalid date format. Use the correct YYYY-MM-DD format");
        }
        if (!isValidBirthday(newUser.getBirthday(), applicationProperties.appConfig().getAge())) {
            throw new UserValidationException("User must be " + applicationProperties.appConfig().getAge()
                    + " years old. Try later");
        }
        return service.saveUser(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody UserRequestDto updateUser) {
        requiredFieldValidation(updateUser);
        if (!isValidDateFormat(updateUser.getBirthday().toString())) {
            throw new UserValidationException("Invalid date format. Use the correct YYYY-MM-DD format");
        }
        if (!isValidBirthday(updateUser.getBirthday(), applicationProperties.appConfig().getAge())) {
            throw new UserValidationException("User must be " + applicationProperties.appConfig().getAge()
                    + " years old. Try later");
        }
        return service.updateUserById(id, updateUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateFields(@PathVariable Long id,
                                             @Valid @RequestBody Map<String, Object> updates) {
        cleanUpdates(updates);
        if (updates.containsKey("email") && !isValidEmail((String) updates.get("email"))) {
            throw new UserValidationException("Invalid email format");
        }
        if (updates.containsKey("birthday") && !isValidDateFormat((String) updates.get("birthday"))) {
            throw new UserValidationException("Invalid date format. Use the correct YYYY-MM-DD format");
        }
        if (updates.containsKey("birthday") && !isValidBirthday(LocalDate.parse((String) updates.get("birthday")),
                applicationProperties.appConfig().getAge())) {
            throw new UserValidationException("User must be " + applicationProperties.appConfig().getAge()
                    + " years old. Try later");
        }
        return service.updateFields(id, updates);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        return service.deleteUserById(id);
    }
}
