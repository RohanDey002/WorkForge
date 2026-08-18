package com.taskmanagement.aitaskmanagement.entity;

public enum Permissions {
    //Admin permissions
        create_user,
        delete_user,
        view_user,
    //Manager permissions
       view_empoyee,
       assign_task,
       update_task,
       delete_task,
    //Employee permissions
      view_task,
      update_task_status
}
