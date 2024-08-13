package com.example.systemtaskmanagement.domain.dao.dto;

import com.example.systemtaskmanagement.domain.dao.model.enums.TaskPriority;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskStatus;
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
public class TaskDTO extends BaseDTO {
    @NotEmpty(message = "title cannot be null or empty!")
    @JsonProperty("title")
    private String title;

    @NotEmpty(message = "description cannot be null or empty!")
    @JsonProperty("description")
    private String description;

    @NotEmpty(message = "task status cannot be null or empty!")
    @JsonProperty("status")
    private TaskStatus status;

    @NotEmpty(message = "task priority cannot be null or empty!")
    @JsonProperty("priority")
    private TaskPriority priority;

    @JsonProperty("author")
    private UserDTO author;

    @JsonProperty("assigned")
    private Set<UserDTO> assignees;


}
