package com.testTasks.testAssignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements Serializable {

    @Schema(description = "User`s Id", example = "5")
    @JsonProperty(value = "id", required = true)
    private Long id;

    @Schema(description = "User`s name", example = "Frank")
    @JsonProperty(value = "first_name", required = true)
    private String firstName;

    @Schema(description = "User`s surname", example = "Wilson")
    @JsonProperty(value = "last_name", required = true)
    private String lastName;

    @Schema(description = "User`s email", example = "carol.jones@example.com")
    @JsonProperty(value = "email", required = true)
    private String email;

    @Schema(description = "User`s birthday", example = "1992-12-24")
    @JsonProperty(value = "birthday", required = true)
    private LocalDate birthday;

    @Schema(description = "User`s address", example = "1415 Cedar Lane")
    @JsonProperty(value = "address")
    private String address;

    @Schema(description = "User`s phone number", example = "567-890-1234")
    @JsonProperty(value = "phone")
    private String phone;

}
