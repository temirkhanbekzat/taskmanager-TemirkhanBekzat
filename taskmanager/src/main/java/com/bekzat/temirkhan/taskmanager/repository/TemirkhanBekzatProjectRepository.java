package com.bekzat.temirkhan.taskmanager.repository;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemirkhanBekzatProjectRepository extends JpaRepository<TemirkhanBekzatProject, Long> {
    List<TemirkhanBekzatProject> findByOwnerId(Long ownerId);
    boolean existsByName(String name);
}
