package com.sh32bit.service;

import com.sh32bit.dto.response.TeacherProfileResponse;

public interface TeacherProfileService {
    TeacherProfileResponse getTeacherProfile(String email);
}
