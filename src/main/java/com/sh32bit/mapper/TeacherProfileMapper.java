package com.sh32bit.mapper;

import com.sh32bit.dto.response.TeacherProfileResponse;
import com.sh32bit.entity.TeacherProfile;

public class TeacherProfileMapper {
    public static TeacherProfileResponse toDTO(TeacherProfile teacherProfile) {
        return TeacherProfileResponse.builder()
                .id(teacherProfile.getId())
                .email(teacherProfile.getUser().getEmail())
                .firstName(teacherProfile.getUser().getFirstName())
                .lastName(teacherProfile.getUser().getLastName())
                .build();
    }
}
