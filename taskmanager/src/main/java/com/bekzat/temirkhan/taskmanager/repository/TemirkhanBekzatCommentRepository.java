package com.bekzat.temirkhan.taskmanager.repository;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemirkhanBekzatCommentRepository extends JpaRepository<TemirkhanBekzatComment, Long> {
    List<TemirkhanBekzatComment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
}
