package com.bekzat.temirkhan.taskmanager.repository;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemirkhanBekzatTaskRepository extends JpaRepository<TemirkhanBekzatTask, Long> {

    List<TemirkhanBekzatTask> findByProjectId(Long projectId);

    List<TemirkhanBekzatTask> findByAssigneeId(Long assigneeId);

    @Query("SELECT t FROM TemirkhanBekzatTask t WHERE " +
           "(:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:projectId IS NULL OR t.project.id = :projectId) AND " +
           "(:assigneeId IS NULL OR t.assignee.id = :assigneeId) AND " +
           "(:categoryId IS NULL OR t.category.id = :categoryId)")
    Page<TemirkhanBekzatTask> findWithFilters(
            @Param("search") String search,
            @Param("status") TemirkhanBekzatTask.TaskStatus status,
            @Param("priority") TemirkhanBekzatTask.Priority priority,
            @Param("projectId") Long projectId,
            @Param("assigneeId") Long assigneeId,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}
