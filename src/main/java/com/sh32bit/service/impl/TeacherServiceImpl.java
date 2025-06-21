package com.sh32bit.service.impl;

import com.sh32bit.dto.response.TeacherProfileResponse;
import com.sh32bit.entity.TeacherProfile;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.mapper.TeacherProfileMapper;
import com.sh32bit.repository.TeacherProfileRepository;
import com.sh32bit.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherProfileRepository teacherProfileRepository;

    @Override
    public TeacherProfileResponse getTeacherProfile(String email) {
        TeacherProfile teacherProfile = teacherProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Teacher Profile Not Found"));

        log.info("Teacher Profile: {}", teacherProfile.toString());

        return TeacherProfileMapper.toDTO(teacherProfile);
    }
}
