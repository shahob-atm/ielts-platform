package com.sh32bit.service;

import com.sh32bit.dto.response.MessageResponse;

public interface ParentService {
    MessageResponse inviteChild(String name, String childEmail);
}
