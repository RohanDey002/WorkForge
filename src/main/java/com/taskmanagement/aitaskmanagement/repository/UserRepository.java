package com.taskmanagement.aitaskmanagement.repository;

import com.taskmanagement.aitaskmanagement.entity.Role;
import com.taskmanagement.aitaskmanagement.entity.User;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByRole(Role role);


    List<User> findByManager(User manager);

    List<User> findByManagerId(Long managerId);

    Optional<User> findByIdAndRole(Long id, Role role);

    @Query("""
          SELECT u
          FROM User u
          LEFT JOIN FETCH u.manager
          WHERE u.id = :id
                """)
    Optional<User> findByIdWithManager(@Param("id") Long id);


}
