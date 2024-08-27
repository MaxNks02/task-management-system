package com.example.systemtaskmanagement.controller;

import com.example.systemtaskmanagement.domain.dao.dto.TaskDTO;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskPriority;
import com.example.systemtaskmanagement.domain.dao.model.enums.TaskStatus;
import com.example.systemtaskmanagement.exeption.handling.ResponseHandler;
import com.example.systemtaskmanagement.payload.request.SaveTaskRequest;
import com.example.systemtaskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Task API", description = "The Task API. Contains operations like get, add, edit, delete.")
@RestController
@RequestMapping("/api/v1/task")

public class TaskController extends BaseController<TaskService> {

    protected TaskController(TaskService service) {
        super(service);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> sortByName() {
        return ResponseHandler.generateResponse("Get all tasks", HttpStatus.OK, service.getAll());
    }

    @GetMapping("/filtered-and-sorted")
    public ResponseEntity<?> getTasksFilteredAndSorted(
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) TaskPriority priority,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseHandler.generateResponse("Get filtered and sorted tasks", HttpStatus.OK, service.getTasksFilteredAndSorted(status, priority, sortBy, sortDirection, page, size));
    }

    @GetMapping("/get-all-by-pagination")
    public ResponseEntity<?> getAllByPagination(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
    return ResponseHandler.generateResponse("get all by pagination", HttpStatus.OK, service.getAllWithPagination(pageable));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody SaveTaskRequest saveTaskRequest) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(saveTaskRequest.getTitle());
        taskDTO.setDescription(saveTaskRequest.getDescription());
        taskDTO.setPriority(saveTaskRequest.getPriority());
        taskDTO.setStatus(saveTaskRequest.getStatus());
        return  ResponseHandler.generateResponse("save task", HttpStatus.OK, service.create(taskDTO));
    }

    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id, @RequestBody SaveTaskRequest saveTaskRequest) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(saveTaskRequest.getTitle());
        taskDTO.setDescription(saveTaskRequest.getDescription());
        taskDTO.setPriority(saveTaskRequest.getPriority());
        taskDTO.setStatus(saveTaskRequest.getStatus());
        return  ResponseHandler.generateResponse("save task", HttpStatus.OK, service.update(id, taskDTO));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
       service.deleteById(id);
       return  ResponseHandler.generateResponse("delete task", HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return ResponseHandler.generateResponse("get task by id", HttpStatus.OK, service.getById(id));
    }

    @GetMapping(value = "/get-user-assigned-tasks/{id}")
    public ResponseEntity<?> getUserAssignedTasks(@PathVariable Long id) {
        return ResponseHandler.generateResponse("get tasks by user id", HttpStatus.OK, service.getUserAssignedTasks(id));
    }

    @GetMapping(value = "/get-user-own-tasks/{id}")
    public ResponseEntity<?> getUserOwnTasks(@PathVariable Long id) {
        return ResponseHandler.generateResponse("get tasks by user id", HttpStatus.OK, service.getUserOwnTasks(id));
    }


    @GetMapping(value = "/get-session-user-assigned-tasks")
    public ResponseEntity<?> getUserAssignedTasks() {
        return ResponseHandler.generateResponse("get session user tasks", HttpStatus.OK, service.getSessionUserAssTask());
    }

    @GetMapping(value = "/get-session-user-own-tasks")
    public ResponseEntity<?> getUserTasks() {
        return ResponseHandler.generateResponse("get session user tasks", HttpStatus.OK, service.getSessionUserOwnTask());
    }

    @PatchMapping(value = "/save-assigned-user")
    public ResponseEntity<?> saveAssignedUser(
            @RequestParam(name = "user_id", required = false, defaultValue = "0") Long userId,
            @RequestParam(name = "task_id", required = false, defaultValue = "0") Long taskId){

      return   ResponseHandler.generateResponse("save assigned user", HttpStatus.OK, service.saveAssignedUser(userId, taskId));
    }

    @PatchMapping(value = "/edit-task-status-assigned-user")
    public ResponseEntity<?> editTaskStatusAssignedUser(@RequestParam Long taskId, @RequestParam TaskStatus taskStatus){
        return ResponseHandler.generateResponse("edit task status assigned user", HttpStatus.OK, service.editStatusTaskOfAssignedUser(taskId, taskStatus));
    }







}
