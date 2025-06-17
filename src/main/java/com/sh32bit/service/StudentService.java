package com.sh32bit.service;

import com.sh32bit.dto.response.AttendanceGradeResponse;
import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.dto.response.MessageResponse;

import java.util.List;

public interface StudentService {
    MessageResponse linkParent(String token, String name);

    List<GroupResponse> getMyGroups(String email);

    List<AttendanceGradeResponse> getStudentAttendances(String email, Long groupId);
}
