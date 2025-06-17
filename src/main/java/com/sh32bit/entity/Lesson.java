package com.sh32bit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Group group;

    @ManyToOne
    private GroupTeacher groupTeacher;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private String topic;

    public Group getGroup() {
        return groupTeacher != null ? groupTeacher.getGroup() : null;
    }

    public TeacherProfile getTeacher() {
        return groupTeacher != null ? groupTeacher.getTeacher() : null;
    }
}
