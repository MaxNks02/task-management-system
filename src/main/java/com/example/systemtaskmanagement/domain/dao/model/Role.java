package com.example.systemtaskmanagement.domain.dao.model;

import com.example.systemtaskmanagement.domain.dao.model.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseModel{


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

}
