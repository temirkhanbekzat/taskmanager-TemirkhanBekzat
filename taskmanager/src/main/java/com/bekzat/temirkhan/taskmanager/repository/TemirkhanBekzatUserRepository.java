package com.bekzat.temirkhan.taskmanager.repository;

import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemirkhanBekzatUserRepository extends JpaRepository<TemirkhanBekzatUser, Long> {
    Optional<TemirkhanBekzatUser> findByUsername(String username);
    Optional<TemirkhanBekzatUser> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
