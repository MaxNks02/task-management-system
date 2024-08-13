package com.example.systemtaskmanagement.domain.dao.model;

import com.example.systemtaskmanagement.domain.dao.model.enums.TaskPriority;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")

public class Task extends BaseModel{

    @Column(name="title")
    @NotBlank
    private String title;


    @Column(name="description")
    @NotBlank
    private String description;

    @Column(name = "status")
    @NotBlank
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "priority")
    @NotBlank
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees = new HashSet<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();



}
