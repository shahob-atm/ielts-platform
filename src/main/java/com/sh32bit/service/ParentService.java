package com.sh32bit.service;

import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.dto.response.StudentResponse;

import java.util.List;

public interface ParentService {
    MessageResponse inviteChild(String name, String childEmail);

    List<StudentResponse> getChildrenOfParent(String email);
}
