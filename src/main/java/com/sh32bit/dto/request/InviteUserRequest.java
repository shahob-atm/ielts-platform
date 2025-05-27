package com.sh32bit.dto.request;

import com.sh32bit.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
