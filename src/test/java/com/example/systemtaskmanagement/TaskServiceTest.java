package com.example.systemtaskmanagement;

import com.example.systemtaskmanagement.domain.dao.dto.TaskDTO;
import com.example.systemtaskmanagement.domain.dao.mapper.TaskMapper;
import com.example.systemtaskmanagement.domain.dao.model.Task;
import com.example.systemtaskmanagement.domain.dao.model.User;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskStatus;
import com.example.systemtaskmanagement.domain.dao.repo.TaskRepo;
import com.example.systemtaskmanagement.domain.dao.repo.UserRepo;
import com.example.systemtaskmanagement.service.TaskService;
import com.example.systemtaskmanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepo taskRepository;

    @Mock
    private UserRepo userRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private User currentUser;
    private Task task;
    private TaskDTO taskDTO;
    private User assignee;
    private Set<TaskDTO> taskSetDTO;
    private Set<Task> taskSet;

    @BeforeEach
    public void setup() {
        currentUser = new User();
        currentUser.setId(1L);

        assignee = new User();
        assignee.setId(2L);

        task = new Task();
        task.setId(1L);
        task.setAuthor(currentUser);
        task.setAssignees(new HashSet<>());

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);

        taskSetDTO = new HashSet<>();
        taskSetDTO.add(taskDTO);

        taskSet = new HashSet<>();
        taskSet.add(task);
    }

    @Test
    public void create_ShouldReturnTaskDTO_WhenSaveIsSuccessful() {
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.create(taskDTO);

        assertNotNull(result);
        assertEquals(taskDTO, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void update_ShouldReturnUpdatedTaskDTO_WhenUpdateIsSuccessful() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.update(1L, taskDTO);

        assertNotNull(result);
        assertEquals(taskDTO, result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void update_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        User anotherUser = new User();
        anotherUser.setId(3L);
        task.setAuthor(anotherUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        assertThrows(AccessDeniedException.class, () -> taskService.update(1L, taskDTO));
    }


    @Test
    public void deleteById_ShouldDeleteTask_WhenUserIsAuthor() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        taskService.deleteById(1L);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    public void deleteById_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {
        User anotherUser = new User();
        anotherUser.setId(3L);
        task.setAuthor(anotherUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        assertThrows(AccessDeniedException.class, () -> taskService.deleteById(1L));
    }

    @Test
    public void getUserAssignedTasks_ShouldReturnAssignedTasks_WhenUserExists() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskMapper.convertFromEntityList(assignee.getAssignedTasks())).thenReturn(taskSetDTO);

        Set<TaskDTO> result = taskService.getUserAssignedTasks(2L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void getUserOwnTasks_ShouldReturnAuthoredTasks_WhenUserExists() {

        Long userId = 1L;
        User user = new User();
        user.setAssignedTasks(Collections.emptySet());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(taskMapper.convertFromEntityList(Collections.emptySet())).thenReturn(new HashSet<>());


        Set<TaskDTO> result = taskService.getUserOwnTasks(userId);


        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void saveAssignedUser_ShouldAddUserToTaskAssignees_WhenUserIsAuthor() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.saveAssignedUser(2L, 1L);

        assertNotNull(result);
        assertTrue(task.getAssignees().contains(assignee));
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void saveAssignedUser_ShouldThrowAccessDeniedException_WhenUserIsNotAuthor() {

        Long userId = 2L;
        Long taskId = 1L;

        User currentUser = new User();
        currentUser.setId(1L);

        Task task = new Task();
        task.setAuthor(new User());
        task.setId(taskId);

        Mockito.when(userService.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            taskService.saveAssignedUser(userId, taskId);
        });
    }
    @Test
    public void editStatusTaskOfAssignedUser_ShouldUpdateStatus_WhenUserIsAssigned() {
        task.getAssignees().add(currentUser);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.editStatusTaskOfAssignedUser(1L, TaskStatus.IN_PROGRESS);

        assertNotNull(result);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void editStatusTaskOfAssignedUser_ShouldThrowAccessDeniedException_WhenUserIsNotAssigned() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.getCurrentUser()).thenReturn(currentUser);

        assertThrows(AccessDeniedException.class, () -> taskService.editStatusTaskOfAssignedUser(1L, TaskStatus.IN_PROGRESS));
    }
}
