package com.sh32bit.dto.request;

import com.sh32bit.enums.Role;
import com.sh32bit.validation.RoleSubset;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteUserRequest {
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Role must not be null")
    @RoleSubset(anyOf = {Role.STUDENT, Role.TEACHER}, message = "Role must be STUDENT or TEACHER")
    private Role role;
}
