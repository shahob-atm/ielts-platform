package com.sh32bit.service;

import com.sh32bit.dto.request.InviteUserRequest;
import com.sh32bit.dto.response.MessageResponse;
import jakarta.mail.MessagingException;

public interface UserService {
    MessageResponse inviteUser(InviteUserRequest req) throws MessagingException;
}
