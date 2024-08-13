package com.example.systemtaskmanagement.controller;

import com.example.systemtaskmanagement.exeption.handling.ResponseHandler;
import com.example.systemtaskmanagement.payload.request.CommentRequest;
import com.example.systemtaskmanagement.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment API", description = "The Comment API. Contains operations like  add, delete.")
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController extends BaseController<CommentService> {

    protected CommentController(CommentService service) {
        super(service);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest) {
        return ResponseHandler.generateResponse("add comment to task", HttpStatus.OK, service.addCommentToTask(commentRequest.getTaskId(), commentRequest.getContent()));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        service.deleteCommentFromTask(id);
        return ResponseHandler.generateResponse("add comment to task", HttpStatus.OK);
    }



}
