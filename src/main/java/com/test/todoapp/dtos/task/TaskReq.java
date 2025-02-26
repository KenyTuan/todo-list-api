package com.test.todoapp.dtos.task;

import com.test.todoapp.constants.MessageException;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class TaskReq {

    @NotBlank(message = MessageException.TITLE_INVALID)
    private String title;

    @NotBlank(message = MessageException.DESCRIPTION_INVALID)
    private String description;

    @NotBlank(message = MessageException.USER_ID_INVALID)
    private String userId;
}
