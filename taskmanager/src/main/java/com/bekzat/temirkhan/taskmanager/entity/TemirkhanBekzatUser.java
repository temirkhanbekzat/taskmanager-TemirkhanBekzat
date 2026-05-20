package com.bekzat.temirkhan.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemirkhanBekzatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_active")
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TemirkhanBekzatTask> assignedTasks = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TemirkhanBekzatTask> createdTasks = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TemirkhanBekzatProject> projects = new ArrayList<>();

    public enum Role {
        USER, ADMIN, MANAGER
    }
}
