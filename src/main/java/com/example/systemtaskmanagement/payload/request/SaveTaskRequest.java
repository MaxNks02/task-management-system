package com.example.systemtaskmanagement.payload.request;

import com.example.systemtaskmanagement.domain.dao.model.enums.TaskPriority;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskRequest {

    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

}
