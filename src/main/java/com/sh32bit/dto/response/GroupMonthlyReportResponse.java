package com.sh32bit.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMonthlyReportResponse {
    private List<Integer> lessonDays;
    private List<StudentAttendanceGradeReport> students;
}
