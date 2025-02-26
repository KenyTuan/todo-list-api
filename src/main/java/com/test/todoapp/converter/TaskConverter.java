package com.test.todoapp.converter;

import com.test.todoapp.dtos.task.TaskReq;
import com.test.todoapp.dtos.task.TaskRes;
import com.test.todoapp.model.entity.Task;
import com.test.todoapp.model.enums.ObjStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskConverter {

    /**
     * Converts a TaskReq to a Task.
     *
     * @param req The TaskReq to convert.
     * @return The converted Task.
     */
    public static Task convertToEntity(TaskReq req) {
        final Task task = Task
                .builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .userId(req.getUserId())
                .build();
        task.setObjStatus(ObjStatus.ACTIVE);
        return task;
    }

    /**
     * Converts a Task to a TaskRes.
     *
     * @param task The Task to convert.
     * @return The converted TaskRes.
     */
    public static TaskRes convertToDto(Task task) {
        return new TaskRes(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getUserId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    /**
     * Converts a list of Task objects to a list of TaskRes objects.
     *
     * @param tasks The list of Task objects to convert.
     * @return The list of converted TaskRes objects.
     */
    public static Set<TaskRes> convertToDtoList(Set<Task> tasks) {
        if (tasks == null) {
            return new HashSet<>();
        }

        return tasks.stream()
                .map(TaskConverter::convertToDto)
                .collect(Collectors.toSet());
    }

    /**
     * Converts a list of TaskReq objects to a list of Task objects.
     *
     * @param reqs The list of TaskReq objects to convert.
     * @return The list of converted Task objects.
     */
    public Set<Task> convertToEntitySet(Set<TaskReq> reqs) {
        return reqs.stream()
                .map(TaskConverter::convertToEntity)
                .collect(Collectors.toSet());
    }
}
