package com.example.systemtaskmanagement.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @JsonProperty("task_id")
    @NotBlank(message = "task id is required")
    private Long taskId;

    @JsonProperty("text")
    @NotBlank(message = "content of the comment is required")
    private String content;

}
