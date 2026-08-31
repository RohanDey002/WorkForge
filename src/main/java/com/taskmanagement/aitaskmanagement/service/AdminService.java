package com.taskmanagement.aitaskmanagement.service;

import com.taskmanagement.aitaskmanagement.DTO.request.RegisterEmployeeRequest;
import com.taskmanagement.aitaskmanagement.DTO.request.RegisterManagerRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import com.taskmanagement.aitaskmanagement.Redis.cacheService.AdminCacheService;
import com.taskmanagement.aitaskmanagement.Redis.cacheService.EmployeeCacheService;
import com.taskmanagement.aitaskmanagement.Redis.cacheService.ManagerCacheService;
import com.taskmanagement.aitaskmanagement.entity.Role;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.exception.BadRequestException;
import com.taskmanagement.aitaskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminCacheService adminCacheService;
    private final ManagerCacheService managerCacheService;
    private final EmployeeCacheService employeeCacheService;

    public UserResponse createManager(RegisterManagerRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email is already existed");
        }
        User manager = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.Manager)
                .build();
        User savedManager = userRepository.save(manager);

        adminCacheService.evictUsers();

        return mapToUserResponse(savedManager);
    }

    public UserResponse createEmployee(RegisterEmployeeRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email is already existed");
        }

        User  manager = userRepository.findByIdAndRole(
                request.getManagerId(),
                Role.Manager
        ).orElseThrow(()->
                new ResourceNotFoundException("Manager not found with ID:"+request.getManagerId())
        );

        User employee = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.Employee)
                .manager(manager)
                .build();

        User savedEmployee = userRepository.save(employee);

        managerCacheService.evictEmployees(manager.getId());

        adminCacheService.evictUsers();

        return mapToUserResponse(savedEmployee);
    }
   public  List<UserResponse> getAllUsers(){

     return adminCacheService.getAllUsers();
   }

   public void deleteUser(Long userId){

        User user = userRepository.findById(userId)
                        .orElseThrow(()->
                                new ResourceNotFoundException("User not found with ID:"+userId));



        userRepository.delete(user);

        adminCacheService.evictUsers();

        if(user.getManager()!=null){

            managerCacheService.getEmployees(user.getManager().getId());
            employeeCacheService.evictEmployeeTask(userId);
        }

        if(user.getRole()==Role.Manager){
            managerCacheService.evictTasks(userId);
            managerCacheService.evictEmployees(userId);
        }

   }

    private UserResponse mapToUserResponse(User user){
        Long managerId = null;

        if(user.getManager()!=null){
            managerId = user.getManager().getId();
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .managerId(managerId)
                .build();
    }

}
