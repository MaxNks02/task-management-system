package com.example.systemtaskmanagement.domain.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO extends BaseDTO {

    @NotEmpty(message = "comment content cannot be null or empty!")
    @JsonProperty("content")
    private String content;

    @NotEmpty(message = "author email cannot be null or empty!")
    @JsonProperty("author")
    private UserDTO author;




}
