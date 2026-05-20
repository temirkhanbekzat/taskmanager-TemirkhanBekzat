package com.bekzat.temirkhan.taskmanager.repository;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemirkhanBekzatAttachmentRepository extends JpaRepository<TemirkhanBekzatAttachment, Long> {
    List<TemirkhanBekzatAttachment> findByTaskId(Long taskId);
}
