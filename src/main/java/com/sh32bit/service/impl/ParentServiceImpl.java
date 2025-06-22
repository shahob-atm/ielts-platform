package com.sh32bit.service.impl;

import com.sh32bit.dto.response.AttendanceGradeResponse;
import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.dto.response.StudentResponse;
import com.sh32bit.entity.*;
import com.sh32bit.enums.GroupStatus;
import com.sh32bit.event.ChildInvitedEvent;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.mapper.AttendanceMapper;
import com.sh32bit.mapper.GroupMapper;
import com.sh32bit.mapper.StudentMapper;
import com.sh32bit.repository.*;
import com.sh32bit.service.ParentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final UserRepository userRepository;
    private final LinkTokenRepository linkTokenRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StudentProfileRepository studentProfileRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final GroupStudentRepository groupStudentRepository;
    private final GroupTeacherRepository groupTeacherRepository;

    @Override
    public MessageResponse inviteChild(String parentEmail, String childEmail) {
        User child = userRepository.findByEmail(childEmail)
                .orElseThrow(() -> new NotFoundException("Child user not found: " + childEmail));

        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new NotFoundException("Parent user not found: " + parentEmail));

        String token = UUID.randomUUID().toString();

        LinkToken linkToken = LinkToken.builder()
                .token(token)
                .parentId(parent.getId())
                .childId(child.getId())
                .createdAt(LocalDateTime.now())
                .build();

        linkTokenRepository.save(linkToken);
        log.info("Child link token: {}", linkToken);

        eventPublisher.publishEvent(new ChildInvitedEvent(parent, child, token));
        log.info("Invitation sent to child {}", child);

        return new MessageResponse("Invitation sent to child");
    }

    @Override
    public List<StudentResponse> getChildrenOfParent(String email) {
        User parent = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Parent user not found: " + email));

        ParentProfile parentProfile = parentProfileRepository.findByUserId(parent.getId())
                .orElseThrow(() -> new NotFoundException("Parent profile not found: " + parent.getId()));

        return parentProfile.getChildren().stream().map(StudentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceGradeResponse> getAttendancesByParent(String email, Long childId, Long groupId) {
        ParentProfile parentProfile = parentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Parent profile not found: " + email));

        StudentProfile studentProfile = studentProfileRepository.findById(childId)
                .orElseThrow(() -> new NotFoundException("Student profile not found: " + childId));

        User user = studentProfile.getUser();

        if (user == null) {
            throw new NotFoundException("User not found: " + email);
        }

        Set<User> children = parentProfile.getChildren();

        if (!children.contains(user)) {
            throw new NotFoundException("This parent does not have such a child!");
        }

        return getAttendanceByStudentIdAndGroupId(studentProfile.getId(), groupId);
    }

    @Override
    public List<GroupResponse> getChildGroups(String email, Long childId) {
        ParentProfile parentProfile = parentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Parent profile not found: " + email));

        User child = userRepository.findById(childId).orElseThrow(() -> new NotFoundException("User not found"));

        if (!parentProfile.getChildren().contains(child)) {
            throw new NotFoundException("This parent does not have such a child!");
        }

        StudentProfile studentProfile = studentProfileRepository.findByUserId(child.getId())
                .orElseThrow(() -> new NotFoundException("Student profile not found: " + child.getId()));

        List<Group> groups = groupStudentRepository.
                findActiveGroupsByStudentId(studentProfile.getId(), GroupStatus.ACTIVE);

        return groups.stream().map(group -> {
            Optional<TeacherProfile> teacherOpt = groupTeacherRepository
                    .findActiveTeacherByGroupId(group.getId());

            TeacherProfile teacher = teacherOpt.orElseGet(() -> {
                User user = new User();
                user.setFirstName("Unknown");
                user.setLastName("Teacher");
                user.setEmail("unknown@email.com");

                TeacherProfile unknown = new TeacherProfile();
                unknown.setId(-1L);
                unknown.setUser(user);
                return unknown;
            });

            return GroupMapper.toDTO(group, teacher);
        }).toList();
    }

    private List<AttendanceGradeResponse> getAttendanceByStudentIdAndGroupId(Long studentId, Long groupId) {
        List<Attendance> attendances = attendanceRepository.findByGroupIdAndStudentId(groupId, studentId);

        List<Long> attendanceIds = attendances.stream()
                .map(Attendance::getId)
                .toList();

        List<Grade> grades = gradeRepository.findByAttendanceIdIn(attendanceIds);

        Map<Long, Grade> gradeMap = grades.stream()
                .collect(Collectors.toMap(
                        g -> g.getAttendance().getId(),
                        g -> g
                ));

        return attendances.stream()
                .map(att -> {
                    Grade grade = gradeMap.get(att.getId());
                    return AttendanceMapper.toDTO(att, grade);
                })
                .toList();
    }
}
