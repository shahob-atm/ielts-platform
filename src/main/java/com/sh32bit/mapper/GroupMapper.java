package com.sh32bit.mapper;

import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.entity.Group;
import com.sh32bit.entity.TeacherProfile;

public class GroupMapper {
    public static GroupResponse toDTO(Group group, TeacherProfile teacher) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .courseName(group.getCourse().getName())
                .teacherId(teacher.getId())
                .firstName(teacher.getUser().getFirstName())
                .lastName(teacher.getUser().getLastName())
                .email(teacher.getUser().getEmail())
                .startDateTime(group.getStartDateTime())
                .endDateTime(group.getEndDateTime())
                .status(group.getStatus())
                .build();
    }
}
