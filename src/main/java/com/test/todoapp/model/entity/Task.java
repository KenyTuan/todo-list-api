package com.test.todoapp.model.entity;

import com.test.todoapp.model.enums.ObjStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class Task {

    @Id
    private String id;

    private String title;

    private String description;

    private String userId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime  updatedAt;

    @Setter
    private ObjStatus objStatus;
}
