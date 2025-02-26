package com.test.todoapp.service;

import com.test.todoapp.dtos.PageRes;
import com.test.todoapp.dtos.task.TaskReq;
import com.test.todoapp.dtos.task.TaskRes;

import java.util.Set;

public interface TaskService {

    Set<TaskRes> getAllTasks();

    TaskRes getTaskById(String id);

    Set<TaskRes> getAllTasksByUserId(String userId);

    TaskRes getTaskByIdAndUserId(String id, String userId);

    PageRes<TaskRes> searchAndFilterTasks(String title, int page, int size,
                                          String sortBy, String sortDir);

    PageRes<TaskRes> searchAndFilterTasksUser(String title,String userId,
                                              int page, int size,
                                              String sortBy, String sortDir);

    TaskRes createTask(TaskReq req);

    TaskRes updateTask(String id, TaskReq req);

    void deleteTaskById(String id);

}
