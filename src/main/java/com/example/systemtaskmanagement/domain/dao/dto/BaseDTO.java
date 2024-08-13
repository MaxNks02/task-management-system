package com.example.systemtaskmanagement.domain.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class BaseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5190598237215532904L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("created_at")
    private String createdAt;
}

