package com.sh32bit.service;

import com.sh32bit.dto.request.ActivateRequest;
import com.sh32bit.dto.request.LoginRequest;
import com.sh32bit.dto.request.ParentRegistrationRequest;
import com.sh32bit.dto.response.LoginResponse;
import com.sh32bit.dto.response.MessageResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    MessageResponse activateUser(ActivateRequest req);

    LoginResponse login(LoginRequest request) throws Exception;

    MessageResponse registerParent(ParentRegistrationRequest req) throws MessagingException;

    MessageResponse activateParent(String token);
}
