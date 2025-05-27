package com.sh32bit.service;

import com.sh32bit.dto.request.ActivateRequest;
import com.sh32bit.dto.request.LoginRequest;
import com.sh32bit.dto.response.LoginResponse;
import com.sh32bit.dto.response.MessageResponse;

public interface AuthService {
    MessageResponse activateUser(ActivateRequest req);

    LoginResponse login(LoginRequest request) throws Exception;
}
