package com.sh32bit.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAttendanceGradeReport {
    private Long studentId;
    private String fullName;
    private List<AttendanceGradeInfo> attendanceList;
}
