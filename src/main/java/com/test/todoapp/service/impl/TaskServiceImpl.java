package com.test.todoapp.service.impl;

import com.test.todoapp.converter.TaskConverter;
import com.test.todoapp.dtos.PageRes;
import com.test.todoapp.dtos.task.TaskReq;
import com.test.todoapp.dtos.task.TaskRes;
import com.test.todoapp.exception.ErrorCode;
import com.test.todoapp.exception.NotFoundException;
import com.test.todoapp.model.entity.Task;
import com.test.todoapp.model.entity.User;
import com.test.todoapp.model.enums.ObjStatus;
import com.test.todoapp.repository.TaskRepository;
import com.test.todoapp.repository.UserRepository;
import com.test.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link TaskService} interface.
 * This class provides methods for managing tasks, including CRUD operations,
 * searching, filtering, and pagination.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    /**
     * Retrieves all active tasks.
     *
     * @return A set of {@link TaskRes} objects representing all active tasks.
     */
    @Override
    public Set<TaskRes> getAllTasks() {
        return TaskConverter.convertToDtoList(new HashSet<>(
                taskRepository.findAllActive()));
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return A {@link TaskRes} object representing the task.
     * @throws NotFoundException If the task is not found.
     */
    @Override
    public TaskRes getTaskById(String id) {
        return TaskConverter.convertToDto(findTaskById(id));
    }

    /**
     * Retrieves all active tasks associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A set of {@link TaskRes} objects representing the user's tasks.
     */
    @Override
    public Set<TaskRes> getAllTasksByUserId(String userId) {
        return TaskConverter.convertToDtoList(new HashSet<>(
                taskRepository.findAllActiveByUserId(userId)
        ));
    }

    /**
     * Retrieves a task by its ID and associated user ID.
     *
     * @param id The ID of the task.
     * @param userId The ID of the user.
     * @return A {@link TaskRes} object representing the task.
     * @throws NotFoundException If the task is not found.
     */
    @Override
    public TaskRes getTaskByIdAndUserId(String id, String userId) {
        return TaskConverter
                .convertToDto(findTaskByIdAndUserId(id, userId));
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
    @Override
    public PageRes<TaskRes> searchAndFilterTasks(String title, int page,
                                                 int size, String sortBy, String sortDir) {
        final Page<Task> tasks = taskRepository
                .findActiveByTitleContaining(
                        title,
                        setupPageRequest(page, size, sortBy, sortDir));

        final Set<TaskRes> taskRes = tasks
                .stream()
                .map(TaskConverter::convertToDto)
                .collect(Collectors.toSet());

        return new PageRes<>(
                taskRes,
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.isLast()
        );
    }

    /**
     * Searches and filters tasks by title and user ID with pagination and sorting.
     *
     * @param title The title to search for.
     * @param userId The ID of the user.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortDir The direction of sorting (ASC or DESC).
     * @return A {@link PageRes} object containing the filtered and paginated tasks.
     */
    @Override
    public PageRes<TaskRes> searchAndFilterTasksUser(String title, String userId,
                                                     int page, int size,
                                                     String sortBy, String sortDir) {
        final Page<Task> tasks = taskRepository
                .findActiveByTitleContainingAndUserId(
                        title, userId,
                        setupPageRequest(page, size, sortBy, sortDir));

        final Set<TaskRes> taskRes = tasks
                .stream()
                .map(TaskConverter::convertToDto)
                .collect(Collectors.toSet());

        return new PageRes<>(
                taskRes,
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.isLast()
        );
    }

    /**
     * Creates a new task and associates it with a user.
     *
     * @param req The task creation request containing task details.
     * @return A {@link TaskRes} object representing the created task.
     */
    @Override
    @Transactional
    public TaskRes createTask(TaskReq req) {
        final User user = findUserById(req.getUserId());
        final Task task = TaskConverter.convertToEntity(req);
        final Task createdTask = taskRepository.save(task);

        user.getTasks().add(createdTask);

        userRepository.save(user);
        return TaskConverter.convertToDto(createdTask);
    }

    /**
     * Updates an existing task.
     *
     * @param id The ID of the task to update.
     * @param req The task update request containing new task details.
     * @return A {@link TaskRes} object representing the updated task.
     */
    @Override
    @Transactional
    public TaskRes updateTask(String id, TaskReq req) {
        final Task task = findTaskById(id);
        final User user = findUserById(req.getUserId());
        final Task newTask = TaskConverter.convertToEntity(req);

        deleteSoftTask(task);

        final Task updatedTask = taskRepository.save(newTask);

        user.getTasks().remove(task);
        user.getTasks().add(updatedTask);

        userRepository.save(user);
        return TaskConverter.convertToDto(updatedTask);
    }

    /**
     * Deletes a task by its ID (soft delete).
     *
     * @param id The ID of the task to delete.
     */
    @Override
    @Transactional
    public void deleteTaskById(String id) {
        final Task task = findTaskById(id);
        final User user = findUserById(task.getUserId());

        deleteSoftTask(task);
        user.getTasks().remove(task);

        userRepository.save(user);
    }

    /**
     * Performs a soft delete on a task by marking it as DELETED.
     *
     * @param task The task to delete.
     */
    private void deleteSoftTask(Task task) {
        task.setObjStatus(ObjStatus.DELETED);
        taskRepository.save(task);
    }

    /**
     * Finds a task by its ID.
     *
     * @param id The ID of the task to find.
     * @return The {@link Task} entity if found.
     * @throws NotFoundException If the task is not found.
     */
    private Task findTaskById(String id) {
        return taskRepository.findTaskActiveById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.TASK_NOT_FOUND.getErrMessage()));
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return The {@link User} entity if found.
     * @throws NotFoundException If the user is not found.
     */
    private User findUserById(String id) {
         return userRepository.findActiveById(id)
                 .orElseThrow(()-> new NotFoundException(
                         ErrorCode.USER_NOT_FOUND.getErrMessage()));
    }

    /**
     * Finds a task by its ID and associated user ID.
     *
     * @param id The ID of the task.
     * @param userId The ID of the user.
     * @return The {@link Task} entity if found.
     * @throws NotFoundException If the task is not found.
     */
    private Task findTaskByIdAndUserId(String id, String userId) {
        return taskRepository
                .findTaskActiveByIdAndUserId(id,userId)
                .orElseThrow(
                        ()-> new NotFoundException(
                                ErrorCode.TASK_NOT_FOUND.getErrMessage())
                );
    }

    /**
     * Sets up pagination and sorting parameters for a query.
     *
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @param sortBy The field to sort by.
     * @param sortDir The direction of sorting (ASC or DESC).
     * @return A {@link PageRequest} object configured with the provided parameters.
     */
    private PageRequest setupPageRequest(int page, int size,
                                         String sortBy, String sortDir) {
        final Sort sort = sortDir.equalsIgnoreCase(
                Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }
}
