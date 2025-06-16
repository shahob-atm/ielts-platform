package com.sh32bit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeMarkRequest {
    @NotNull(message = "attendanceId must be not null")
    private Long attendanceId;

    @NotNull(message = "score must be not null")
    private Double score;

    private String gradeComment;
}
