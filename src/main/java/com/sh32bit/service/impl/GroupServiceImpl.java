package com.sh32bit.service.impl;

import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.entity.Group;
import com.sh32bit.entity.TeacherProfile;
import com.sh32bit.entity.User;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.mapper.GroupMapper;
import com.sh32bit.repository.GroupRepository;
import com.sh32bit.repository.TeacherProfileRepository;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    @Override
    public List<GroupResponse> getGroupsByTeacher(String email) {
        User teacherUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
        log.info("user id: {}", teacherUser.getId());

        TeacherProfile profile = teacherProfileRepository.findByUserId(teacherUser.getId())
                .orElseThrow(() -> new NotFoundException("Teacher profile not found!"));
        log.info("profile id: {}", profile.getId());

        List<Group> groups = groupRepository.findAllByTeacher(profile);
        log.info("groups size: {}", groups.size());

        return groups.stream().map(GroupMapper::toDTO).collect(Collectors.toList());
    }
}
