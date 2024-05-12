package com.testTasks.testAssignment.rest;

import com.testTasks.testAssignment.config.ApplicationProperties;
import com.testTasks.testAssignment.exception.ApiError;
import com.testTasks.testAssignment.exception.UserValidationException;
import com.testTasks.testAssignment.model.User;
import com.testTasks.testAssignment.model.UserRequestDto;
import com.testTasks.testAssignment.model.UserResponseDto;
import com.testTasks.testAssignment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;
    private final ApplicationProperties applicationProperties;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @Operation(summary = "Obtaining data about the user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        log.info("Fetching user by ID: {}", id);
        return service.findUserById(id);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @Parameters({
            @Parameter(name = "from", description = "Date of birth from which to search for users."),
            @Parameter(name = "to", description = "Date of birth to search for users."),
            @Parameter(name = "limit", description = "Quantity of returned records. Default value = 3"),
            @Parameter(name = "offset", description = "Offset (from which element to return). Default value = 0")
    })
    @Operation(summary = "Obtaining users data with filter")
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
        log.info("Fetching all users from {} to {}, limit {}, offset {}", from, to, limit, offset);
        return service.findAllUsers(from, to, limit, offset);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @Operation(summary = "Create a new user")
    @PostMapping("/")
    public ResponseEntity<UserResponseDto> createNew(@Valid @RequestBody UserRequestDto newUser) {
        requiredFieldValidation(newUser);
        if (!isValidBirthday(newUser.getBirthday(), applicationProperties.appConfig().getAge())) {
            throw new UserValidationException("User must be " + applicationProperties.appConfig().getAge()
                    + " years old. Try later");
        }
        log.info("Creating a new user with email: {}", newUser.getEmail());
        return service.saveUser(newUser);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @Operation(summary = "Updating an existing user")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserRequestDto updateUser) {
        requiredFieldValidation(updateUser);
        if (!isValidEmail(updateUser.getEmail())) {
            throw new UserValidationException("Invalid email format");
        }
        if (!isValidBirthday(updateUser.getBirthday(), applicationProperties.appConfig().getAge())) {
            throw new UserValidationException("User must be " + applicationProperties.appConfig().getAge()
                    + " years old and the value must be before the current date.");
        }
        log.info("Updating user ID: {}", id);
        return service.updateUserById(id, updateUser);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })

    @Operation(summary = "Update user details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example",
                                    value = "{\"first_name\": \"John\", \"last_name\": \"Doe\"," +
                                            " \"email\": \"john.doe@example.com\", \"birthday\": \"1990-01-01\"," +
                                            " \"address\": \"1415 Cedar Lane\", \"phone\": \"567-890-123\"}"
                            )
                    )
            )
    )
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateFields(@PathVariable Long id,
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
        log.info("Updating fields for user ID: {}", id);
        return service.updateFields(id, updates);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @Operation(summary = "Soft delete of an existing user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        log.info("Deleting user ID: {}", id);
        return service.deleteUserById(id);
    }
}
