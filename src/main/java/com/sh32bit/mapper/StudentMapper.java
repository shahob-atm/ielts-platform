package com.sh32bit.mapper;

import com.sh32bit.dto.response.StudentResponse;
import com.sh32bit.entity.User;

public class StudentMapper {
    public static StudentResponse toDTO(User student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
