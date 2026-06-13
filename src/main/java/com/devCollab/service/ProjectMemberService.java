package com.devCollab.service;

import com.devCollab.dto.InviteMemberRequest;
import com.devCollab.dto.MemberResponse;
import com.devCollab.entity.*;
import com.devCollab.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (memberRepository.existsByProjectIdAndUserId(projectId, request.getUserId()))
            throw new RuntimeException("User is already a member");

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(request.getRole() != null ? request.getRole() : MemberRole.MEMBER)
                .build();

        ProjectMember savedMember = memberRepository.save(member);
        activityService.logAndNotify(project, getCurrentUser(), "invited", "Member", user.getUsername());
        return toResponse(savedMember);
    }

    public List<MemberResponse> getMembers(Long projectId) {
        return memberRepository.findByProjectId(projectId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse updateMemberRole(Long projectId, Long userId, MemberRole role) {
        ProjectMember member = memberRepository
                .findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setRole(role);
        return toResponse(memberRepository.save(member));
    }

    public void removeMember(Long projectId, Long userId) {
        ProjectMember member = memberRepository
                .findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        memberRepository.delete(member);
    }

    public List<MemberResponse> getMyProjects() {
        User user = getCurrentUser();
        return memberRepository.findByUserId(user.getId())
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public boolean isMember(Long projectId, Long userId) {
        return memberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    private MemberResponse toResponse(ProjectMember member) {
        MemberResponse res = new MemberResponse();
        res.setId(member.getId());
        res.setUserId(member.getUser().getId());
        res.setUsername(member.getUser().getUsername());
        res.setEmail(member.getUser().getEmail());
        res.setRole(member.getRole());
        res.setJoinedAt(member.getJoinedAt());
        return res;
    }
}