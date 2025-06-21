package com.sh32bit.service;

import com.sh32bit.dto.response.TeacherProfileResponse;

public interface TeacherService {
    TeacherProfileResponse getTeacherProfile(String email);
}
