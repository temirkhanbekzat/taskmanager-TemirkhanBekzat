package com.bekzat.temirkhan.taskmanager.repository;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemirkhanBekzatCategoryRepository extends JpaRepository<TemirkhanBekzatCategory, Long> {
    Optional<TemirkhanBekzatCategory> findByName(String name);
    boolean existsByName(String name);
}
