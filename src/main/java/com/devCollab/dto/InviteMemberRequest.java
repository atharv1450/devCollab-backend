package com.devCollab.dto;

import com.devCollab.entity.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InviteMemberRequest {
    @NotNull
    private Long userId;
    private MemberRole role = MemberRole.MEMBER;
}
