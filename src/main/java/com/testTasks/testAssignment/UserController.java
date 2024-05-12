package com.testTasks.testAssignment;

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
import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;
    private final Config config;

    @Operation(summary = "Obtaining data about the user by id")
    @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = UserDto.class))})
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return service.findUserById(id);
    }

    @Operation(summary = "Obtaining user data")
    @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = UserDto.class))})
    @Parameters({
            @Parameter(name = "from", description = "Date of birth from which to search for users."),
            @Parameter(name = "to", description = "Date of birth to search for users."),
            @Parameter(name = "limit", description = "Quantity of returned records. Default value = 3"),
            @Parameter(name = "offset", description = "Offset (from which element to return). Default value = 0")
    })
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(required = false, name = "from") LocalDate from,
            @RequestParam(required = false, name = "to") LocalDate to,
            @RequestParam(required = false, name = "limit", defaultValue = "3") Integer limit,
            @RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset) {
        if (isNull(to)) {
            to = LocalDate.now().minusYears(config.appConfig().getAge());
        }
        if (isNull(from)) {
            from = LocalDate.parse(config.appConfig().getMinBirthday());
        }
        if (from.isAfter(to)) {
            throw new UserNotFoundException(1L
//                    "Parameter 'from' cannot be after 'to'"
            );
        }
        return service.findAllUsers(from, to, limit, offset);
    }

    @PostMapping("/")
    public ResponseEntity<User> createNew(@Valid @RequestBody User newUser) {
        return service.saveUser(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody User updateUser) {
        return service.updateUserById(id, updateUser);
    }

    @PatchMapping("/")
    public ResponseEntity<User> updateFields(@Valid @RequestBody User newUser) {
        return service.updateFields(newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        return service.deleteUserById(id);
    }
}
