package com.sh32bit.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceGradeInfo {
    private Long lessonId;
    private Long attendanceId;
    private Long gradeId;
    private LocalDate lessonDate;
    private String topic;
    private Boolean present;
    private String attendanceComment;
    private Double grade;
    private String gradeComment;
    private LocalTime lessonStartTime;
    private LocalTime lessonEndTime;
    private boolean archived;
}
