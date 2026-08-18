package com.taskmanagement.aitaskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    @JsonIgnore
    private User assignedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    @JsonIgnore
    private User assignedTo;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @PrePersist
    public void onCreate(){
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();

        if(status==null) status = TaskStatus.TODO;
    }
    @PreUpdate
    public void onUpdate(){
        updateAt = LocalDateTime.now();
    }
}
