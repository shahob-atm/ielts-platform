package com.sh32bit.service;

import com.sh32bit.dto.response.MessageResponse;

public interface StudentService {
    MessageResponse linkParent(String token, String name);
}
