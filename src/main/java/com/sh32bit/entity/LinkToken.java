package com.sh32bit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String token;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private Long childId;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
