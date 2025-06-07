package com.sh32bit.mapper;

import com.sh32bit.dto.request.InviteUserRequest;
import com.sh32bit.entity.User;

public class UserMapper {
    public static User toEntity(InviteUserRequest req) {
        return User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .role(req.getRole())
                .enabled(false)
                .build();
    }
}
