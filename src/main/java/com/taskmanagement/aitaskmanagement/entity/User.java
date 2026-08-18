package com.taskmanagement.aitaskmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private  String name;

    private String password;

    @Column(nullable = false, unique = true)
    private  String email;

    @Enumerated(EnumType.STRING)
    private  Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private User manager;

    @OneToMany(mappedBy = "manager")
    private List<User> employees = new ArrayList<>();

    @OneToMany(mappedBy = "assignedBy")
    private List<Task> assignedTasks = new ArrayList<>();

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> receivedTasks = new ArrayList<>();


}
