package com.sh32bit.service;

import com.sh32bit.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getGroupsByTeacher(String email);
}
