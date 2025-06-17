package com.sh32bit.service.impl;

import com.sh32bit.dto.response.AttendanceGradeResponse;
import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.*;
import com.sh32bit.enums.GroupStatus;
import com.sh32bit.exception.ConflictException;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.mapper.AttendanceMapper;
import com.sh32bit.mapper.GroupMapper;
import com.sh32bit.repository.*;
import com.sh32bit.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;
    private final LinkTokenRepository linkTokenRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final GroupStudentRepository groupStudentRepository;
    private final GroupTeacherRepository groupTeacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;

    @Transactional
    public MessageResponse linkParent(String token, String childEmail) {
        LinkToken linkToken = linkTokenRepository.findByToken(token)
                .orElseThrow(() -> new ConflictException("Invalid token"));

        if (linkToken.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Token expired");
        }

        User child = userRepository.findByEmail(childEmail)
                .orElseThrow(() -> new NotFoundException(childEmail));

        if (!linkToken.getChildId().equals(child.getId())) {
            throw new ConflictException("Token does not match this user");
        }

        ParentProfile parentProfile = parentProfileRepository.findByUserId(linkToken.getParentId())
                .orElseThrow(() -> new NotFoundException("ParentProfile userId " + linkToken.getParentId()));

        if (parentProfile.getChildren().contains(child)) {
            throw new ConflictException("This child is already linked to the parent");
        }

        parentProfile.getChildren().add(child);
        parentProfileRepository.save(parentProfile);
        log.info("Student and Parent is connected");

        linkTokenRepository.delete(linkToken);
        log.info("linkToken deleted: {}", linkToken);

        return new MessageResponse("Parent linked successfully");
    }

    @Override
    public List<GroupResponse> getMyGroups(String email) {
        StudentProfile studentProfile = studentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("StudentProfile not found"));

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

    @Override
    public List<AttendanceGradeResponse> getStudentAttendances(String email, Long groupId) {
        StudentProfile studentProfile = studentProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("StudentProfile not found"));

        List<Attendance> attendances = attendanceRepository.findByGroupIdAndStudentId(groupId, studentProfile.getId());

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
