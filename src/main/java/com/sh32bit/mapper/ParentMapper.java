package com.sh32bit.mapper;

import com.sh32bit.dto.request.ParentRegistrationRequest;
import com.sh32bit.entity.User;
import com.sh32bit.enums.Role;

public class ParentMapper {
    public static User toEntity(ParentRegistrationRequest req) {
        return User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .role(Role.PARENT)
                .enabled(false)
                .build();
    }
}
