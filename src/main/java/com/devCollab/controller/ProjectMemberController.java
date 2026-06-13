package com.devCollab.controller;

import com.devCollab.dto.InviteMemberRequest;
import com.devCollab.dto.MemberResponse;
import com.devCollab.entity.MemberRole;
import com.devCollab.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(
            @PathVariable Long projectId,
            @Valid @RequestBody InviteMemberRequest request) {
        return ResponseEntity.ok(memberService.inviteMember(projectId, request));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers(@PathVariable Long projectId) {
        return ResponseEntity.ok(memberService.getMembers(projectId));
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<MemberResponse> updateRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestParam MemberRole role) {
        return ResponseEntity.ok(memberService.updateMemberRole(projectId, userId, role));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        memberService.removeMember(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}
