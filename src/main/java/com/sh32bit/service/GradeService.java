package com.sh32bit.service;

import com.sh32bit.dto.request.GradeMarkRequest;
import com.sh32bit.dto.response.MessageResponse;
import jakarta.validation.Valid;

public interface GradeService {
    MessageResponse gradeMark(Long id, @Valid GradeMarkRequest request, String email);
}
