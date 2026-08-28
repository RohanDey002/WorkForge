package com.taskmanagement.aitaskmanagement.entity;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Role {
    Admin(Set.of(
            Permissions.view_user,
            Permissions.create_user,
            Permissions.delete_user
    )),
    Manager(Set.of(
          Permissions.view_empoyee,
          Permissions.assign_task,
          Permissions.delete_task,
          Permissions.update_task
    )),
    Employee(Set.of(
            Permissions.view_task,
            Permissions.update_task_status
    ));


    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {

        this.permissions = permissions;
    }
}
