package com.devCollab.dto;

import com.devCollab.entity.MemberRole;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MemberResponse {
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private MemberRole role;
    private LocalDateTime joinedAt;
}
