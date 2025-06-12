package com.sh32bit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupTeacher {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Group group;

    @ManyToOne
    private TeacherProfile teacher;

    private LocalDate joinDate;
    private LocalDate leaveDate;
}
