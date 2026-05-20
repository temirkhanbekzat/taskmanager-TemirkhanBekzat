package com.bekzat.temirkhan.taskmanager.service.impl;

import com.bekzat.temirkhan.taskmanager.dto.request.TemirkhanBekzatProjectRequest;
import com.bekzat.temirkhan.taskmanager.dto.response.TemirkhanBekzatProjectResponse;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatProject;
import com.bekzat.temirkhan.taskmanager.entity.TemirkhanBekzatUser;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatBadRequestException;
import com.bekzat.temirkhan.taskmanager.exception.TemirkhanBekzatResourceNotFoundException;
import com.bekzat.temirkhan.taskmanager.mapper.TemirkhanBekzatProjectMapper;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatProjectRepository;
import com.bekzat.temirkhan.taskmanager.repository.TemirkhanBekzatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemirkhanBekzatProjectServiceImpl {

    private final TemirkhanBekzatProjectRepository projectRepository;
    private final TemirkhanBekzatUserRepository userRepository;
    private final TemirkhanBekzatProjectMapper projectMapper;

    @Transactional
    public TemirkhanBekzatProjectResponse createProject(TemirkhanBekzatProjectRequest request, String username) {
        log.info("Creating project '{}' by user: {}", request.getName(), username);
        if (projectRepository.existsByName(request.getName())) {
            throw new TemirkhanBekzatBadRequestException("Project with name '" + request.getName() + "' already exists");
        }
        TemirkhanBekzatUser owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found: " + username));

        TemirkhanBekzatProject project = TemirkhanBekzatProject.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TemirkhanBekzatProject.ProjectStatus.ACTIVE)
                .owner(owner)
                .build();

        TemirkhanBekzatProject saved = projectRepository.save(project);
        log.info("Project created with id: {}", saved.getId());
        return projectMapper.toResponse(saved);
    }

    public TemirkhanBekzatProjectResponse getProjectById(Long id) {
        TemirkhanBekzatProject project = projectRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Project not found with id: " + id));
        return projectMapper.toResponse(project);
    }

    public List<TemirkhanBekzatProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TemirkhanBekzatProjectResponse> getMyProjects(String username) {
        TemirkhanBekzatUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("User not found"));
        return projectRepository.findByOwnerId(user.getId()).stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TemirkhanBekzatProjectResponse updateProject(Long id, TemirkhanBekzatProjectRequest request) {
        log.info("Updating project id: {}", id);
        TemirkhanBekzatProject project = projectRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Project not found with id: " + id));
        project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(request.getStatus());
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long id) {
        log.info("Deleting project id: {}", id);
        TemirkhanBekzatProject project = projectRepository.findById(id)
                .orElseThrow(() -> new TemirkhanBekzatResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
    }
}
