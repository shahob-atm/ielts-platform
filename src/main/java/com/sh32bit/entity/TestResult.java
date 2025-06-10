package com.sh32bit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Test test;

    @ManyToOne
    private StudentProfile student;

    private double listening;
    private double reading;
    private double writing;
    private double speaking;
    private double totalScore;
}
