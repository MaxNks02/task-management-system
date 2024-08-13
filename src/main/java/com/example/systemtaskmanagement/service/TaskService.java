package com.example.systemtaskmanagement.service;

import com.example.systemtaskmanagement.domain.dao.dto.TaskDTO;
import com.example.systemtaskmanagement.domain.dao.mapper.TaskMapper;
import com.example.systemtaskmanagement.domain.dao.model.Task;
import com.example.systemtaskmanagement.domain.dao.model.User;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskPriority;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskStatus;
import com.example.systemtaskmanagement.domain.dao.repo.TaskRepo;
import com.example.systemtaskmanagement.domain.dao.repo.UserRepo;
import com.example.systemtaskmanagement.domain.query.TaskSpecification;
import com.example.systemtaskmanagement.exeption.CustomNotFoundException;
import com.example.systemtaskmanagement.exeption.DatabaseException;
import com.example.systemtaskmanagement.exeption.handling.ApiMessages;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;


@Service
public class TaskService extends BaseService<Task, TaskDTO, TaskRepo, TaskMapper> {

    ;
    private final UserService userService;
    private final UserRepo userRepo;

    protected TaskService(TaskRepo repository, TaskMapper mapper, UserService userService, UserRepo userRepo) {
        super(repository, mapper);
        this.userService = userService;

        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public TaskDTO create(TaskDTO dto) {
        Task entity = getMapper().toEntity(dto);
        Task savedEntity;
        User currentUser = userService.getCurrentUser();
        try {
            entity.setAuthor(currentUser);
            savedEntity = getRepository().save(entity);
        } catch (RuntimeException exception) {
            throw new DatabaseException(String.format(ApiMessages.INTERNAL_SERVER_ERROR + " %s", exception.getMessage()));
        }
        return getMapper().toDto(savedEntity);
    }

    public Page<TaskDTO> getTasksFilteredAndSorted(
            TaskStatus status,
            TaskPriority priority,
            String sortBy,
            String sortDirection,
            int page,
            int size) {

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "createdAt";
        }

        Pageable pageable = PageRequest.of(page, size);
        Specification<Task> spec = TaskSpecification.filterAndSort(status, priority, sortBy, sortDirection);

        return getRepository().findAll(spec, pageable).map(getMapper()::toDto);
    }


    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        Task task = getRepository().findById(id)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, dto.getId())));

        User currentUser = userService.getCurrentUser();

        if (!task.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException(String.format(ApiMessages.ACCESS_DENIED));
        }
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setAuthor(currentUser);
        getRepository().save(task);
        return getMapper().toDto(task);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Task task = getRepository().findById(id)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, id)));

        User currentUser = userService.getCurrentUser();

        if (!task.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException(String.format(ApiMessages.ACCESS_DENIED));
        }
        getRepository().delete(task);
    }



    public Set<TaskDTO> getUserAssignedTasks(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, userId)));
        return getMapper().convertFromEntityList(user.getAssignedTasks());
    }

    public Set<TaskDTO> getUserOwnTasks(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, userId)));
        return getMapper().convertFromEntityList(user.getAssignedTasks());
    }

    public Set<TaskDTO> getSessionUserAssTask() {
        User user = userService.getCurrentUser();
        return getMapper().convertFromEntityList(user.getAssignedTasks());
    }

    public Set<TaskDTO> getSessionUserOwnTask() {
        User user = userService.getCurrentUser();
        return getMapper().convertFromEntityList(user.getAuthoredTasks());
    }

    public TaskDTO saveAssignedUser(Long userId, Long taskId) {
        Task task = getRepository().findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, taskId)));
        System.out.println(task.getAuthor().getId());
        User currentUser = userService.getCurrentUser();

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, userId)));


        if (!Objects.equals(task.getAuthor().getId(), currentUser.getId())) {
            throw new AccessDeniedException(String.format(ApiMessages.ACCESS_DENIED));
        }
        Set<User> assignedUsers = task.getAssignees();
        assignedUsers.add(user);
        task.setAssignees(assignedUsers);
        getRepository().save(task);
        return getMapper().toDto(task);
    }

    public TaskDTO editStatusTaskOfAssignedUser(Long taskId, TaskStatus status) {
        Task task = getRepository().findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, taskId)));


        User currentUser = userService.getCurrentUser();

        if (!task.getAssignees().contains(currentUser)) {
            throw new AccessDeniedException(ApiMessages.ACCESS_DENIED);
        }
        task.setStatus(status);
        getRepository().save(task);
        return getMapper().toDto(task);
    }
    @Override
    public TaskDTO update(TaskDTO dto) {
        return null;
    }
}
