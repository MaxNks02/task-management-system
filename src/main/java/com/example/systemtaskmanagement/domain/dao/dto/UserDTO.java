package com.example.systemtaskmanagement.domain.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO {

    @NotEmpty(message = "user email cannot be null or empty!")
    @JsonProperty("email")
    private String email;

    @NotEmpty(message = "user password cannot be null or empty!")
    @JsonProperty("password")
    private String password;

}
