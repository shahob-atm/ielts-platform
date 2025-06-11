package com.sh32bit.dto.response;

import com.sh32bit.enums.GroupStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupResponse {
    private Long id;
    private String name;
    private String courseName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private GroupStatus status;
    private Long teacherId;
    private String firstName;
    private String lastName;
    private String email;
}
