package com.taskmanagement.aitaskmanagement.Redis.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisSession {
    private Long userId;
    private String email;
    private Long sessionStart;
    private Long lastRefresh;
    private boolean revoked;
}
