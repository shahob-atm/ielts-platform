package com.sh32bit.service;

import com.sh32bit.dto.response.GroupMonthlyReportResponse;
import com.sh32bit.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getGroupsByTeacher(String email);

    GroupMonthlyReportResponse getGroupMonthReport(Long groupId, int year, int month);
}
