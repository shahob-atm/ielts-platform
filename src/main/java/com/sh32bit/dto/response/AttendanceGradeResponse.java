package com.sh32bit.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceGradeResponse {
    private Long attendanceId;
    private String firstName;
    private String lastName;
    private boolean present;
    private String comment;
    private boolean archived;
    private LocalDate date;
    private String topic;
    private String gradeComment;
    private Double gradeScore;
}
