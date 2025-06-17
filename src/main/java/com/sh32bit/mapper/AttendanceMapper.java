package com.sh32bit.mapper;

import com.sh32bit.dto.response.AttendanceGradeResponse;
import com.sh32bit.entity.Attendance;
import com.sh32bit.entity.Grade;

public class AttendanceMapper {
    public static AttendanceGradeResponse toDTO(Attendance att, Grade grade) {
        AttendanceGradeResponse dto = new AttendanceGradeResponse();
        dto.setAttendanceId(att.getId());
        dto.setFirstName(att.getStudent().getUser().getFirstName());
        dto.setLastName(att.getStudent().getUser().getLastName());
        dto.setPresent(att.isPresent());
        dto.setComment(att.getComment());
        dto.setArchived(att.isArchived());
        dto.setDate(att.getLesson().getDate());
        dto.setTopic(att.getLesson().getTopic());

        if (grade != null) {
            dto.setGradeScore(grade.getScore());
            dto.setGradeComment(grade.getComment());
        }

        return dto;
    }
}
