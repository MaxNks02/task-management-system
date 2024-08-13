package com.example.systemtaskmanagement.service;

import com.example.systemtaskmanagement.domain.dao.dto.CommentDTO;
import com.example.systemtaskmanagement.domain.dao.mapper.CommentMapper;
import com.example.systemtaskmanagement.domain.dao.model.Comment;
import com.example.systemtaskmanagement.domain.dao.model.Task;
import com.example.systemtaskmanagement.domain.dao.model.User;
import com.example.systemtaskmanagement.domain.dao.repo.CommentRepo;
import com.example.systemtaskmanagement.domain.dao.repo.TaskRepo;
import com.example.systemtaskmanagement.exeption.CustomNotFoundException;
import com.example.systemtaskmanagement.exeption.handling.ApiMessages;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CommentService extends BaseService<Comment, CommentDTO, CommentRepo, CommentMapper> {

    private final TaskRepo taskRepository;
    private final UserService userService;
    protected CommentService(CommentRepo repository,  CommentMapper mapper, TaskRepo taskRepository, UserService userService) {
        super(repository, mapper);
        this.taskRepository = taskRepository;
        this.userService = userService;
    }


    @Transactional
    public CommentDTO addCommentToTask(Long taskId, String content) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, taskId)));
        User currentUser = userService.getCurrentUser();

        Comment comment = Comment.builder()
                .content(content)
                .task(task)
                .author(currentUser)
                .build();

        getRepository().save(comment);
        return getMapper().toDto(comment);
    }

    @Transactional
    public void deleteCommentFromTask(Long commentId) {
        Comment comment  = getRepository().findById(commentId)
                .orElseThrow(() -> new CustomNotFoundException(String.format(ApiMessages.NOT_FOUND + DATA_NOT_FOUND, commentId)));
        User currentUser = userService.getCurrentUser();

        if (!comment.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException(String.format(ApiMessages.ACCESS_DENIED));
        }
        getRepository().deleteById(commentId);

    }

    @Override
    public CommentDTO update(CommentDTO dto) {
        return null;
    }
}
