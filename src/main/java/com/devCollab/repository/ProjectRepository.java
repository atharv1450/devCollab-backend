package com.devCollab.repository;

import com.devCollab.entity.Project;
import com.devCollab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwner(User owner);
}
