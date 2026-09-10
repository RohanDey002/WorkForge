package com.taskmanagement.aitaskmanagement.Redis.cacheService;

import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import com.taskmanagement.aitaskmanagement.Redis.Presence.PresenceServices;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCacheService {

    private final UserRepository userRepository;




    @Cacheable(
            cacheNames = "allUsers",
            key = "'all'"
    )
    public List<UserResponse> getAllUsers(){

        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @CacheEvict(
            cacheNames = "allUsers",
            allEntries = true
    )
    public void evictUsers(){
        System.out.println(
                "CACHE EVICT----- users"
        );
    }


    private UserResponse mapToUserResponse(User user){

        Long manageId = user.getManager()!=null?
                user.getManager().getId():null;



        return  UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .managerId(manageId)
                .role(user.getRole())
                .build();
    }

}
