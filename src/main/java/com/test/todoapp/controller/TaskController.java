package com.test.todoapp.controller;

import com.test.todoapp.constants.APIEndPoint;
import com.test.todoapp.dtos.PageRes;
import com.test.todoapp.dtos.task.TaskReq;
import com.test.todoapp.dtos.task.TaskRes;
import com.test.todoapp.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIEndPoint.PREFIX)
public class TaskController {

    private final TaskService taskService;

    /**
     * Retrieves all active tasks.
     *
     * @return A set of {@link TaskRes} objects representing all active tasks.
     */
    @GetMapping(APIEndPoint.TASK_V1)
    public Set<TaskRes> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return A {@link TaskRes} object representing the task.
     */
    @GetMapping(APIEndPoint.TASK_V1 + "/{id}")
    public TaskRes getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    /**
     * Retrieves all tasks associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A set of {@link TaskRes} objects representing the user's tasks.
     */
    @GetMapping(APIEndPoint.TASK_V1 + "/user/{userId}")
    public Set<TaskRes> getTasksByUserId(@PathVariable String userId) {
        return taskService.getAllTasksByUserId(userId);
    }

    /**
     * Retrieves a task by its ID and associated user ID.
     *
     * @param id The ID of the task.
     * @param userId The ID of the user.
     * @return A {@link TaskRes} object representing the task.
     */
    @GetMapping(APIEndPoint.TASK_V1 + "/{id}/user/{userId}")
    public TaskRes getTaskByIdAndUserId(
            @PathVariable String id,
            @PathVariable String userId) {
        return taskService.getTaskByIdAndUserId(id,userId);
    }

    /**
     * Searches and filters tasks by title with pagination and sorting.
     *
     * @param title The title to search for.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortDir The direction of sorting (ASC or DESC).
     * @return A {@link PageRes} object containing the filtered and paginated tasks.
     */
    @GetMapping(APIEndPoint.TASK_V1 + "/search")
    public PageRes<TaskRes> searchAndFilterTasks(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortBy,
            @RequestParam(defaultValue = "title") String sortDir) {
        return taskService
                .searchAndFilterTasks(title, page, size, sortBy, sortDir);
    }

    /**
     * Searches and filters tasks by title and user ID with pagination and sorting.
     *
     * @param userId The ID of the user.
     * @param title The title to search for.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortDir The direction of sorting (ASC or DESC).
     * @return A {@link PageRes} object containing the filtered and paginated tasks.
     */
    @GetMapping(APIEndPoint.TASK_V1 + "/user/{userId}/search")
    public PageRes<TaskRes> searchAndFilterTasksByUserId(
            @PathVariable String userId,
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortBy,
            @RequestParam(defaultValue = "title") String sortDir
    ){
        return taskService.searchAndFilterTasksUser(
                title, userId, page, size, sortBy, sortDir);
    }

    /**
     * Creates a new task.
     *
     * @param taskReq The task creation request containing task details.
     * @return A {@link TaskRes} object representing the created task.
     */
    @PreAuthorize("hasAuthority('LEADER')")
    @PostMapping(APIEndPoint.TASK_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public TaskRes addTask(@Valid @RequestBody TaskReq taskReq) {
        return taskService.createTask(taskReq);
    }

    /**
     * Updates an existing task.
     *
     * @param id The ID of the task to update.
     * @param taskReq The task update request containing new task details.
     * @return A {@link TaskRes} object representing the updated task.
     */
    @PreAuthorize("hasAuthority('LEADER')")
    @PutMapping(APIEndPoint.TASK_V1 + "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskRes updateTask(@Valid @RequestBody TaskReq taskReq, @PathVariable String id) {
        return taskService.updateTask(id,taskReq);
    }

    /**
     * Deletes a task by its ID (soft delete).
     *
     * @param id The ID of the task to delete.
     */
    @PreAuthorize("hasAuthority('LEADER')")
    @DeleteMapping(APIEndPoint.TASK_V1 + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask( @PathVariable String id) {
        taskService.deleteTaskById(id);
    }
}
