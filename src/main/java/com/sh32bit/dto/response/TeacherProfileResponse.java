package com.sh32bit.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
