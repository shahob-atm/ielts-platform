package com.sh32bit.service.impl;

import com.sh32bit.dto.response.AttendanceGradeInfo;
import com.sh32bit.dto.response.GroupMonthlyReportResponse;
import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.dto.response.StudentAttendanceGradeReport;
import com.sh32bit.entity.*;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.mapper.GroupMapper;
import com.sh32bit.repository.*;
import com.sh32bit.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;

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

    @Override
    public GroupMonthlyReportResponse getGroupMonthReport(Long groupId, int year, int month) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        Set<StudentProfile> students = group.getStudents();
        if (students.isEmpty()) return new GroupMonthlyReportResponse(Collections.emptyList(), Collections.emptyList());

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Lesson> lessons = lessonRepository.findByGroupIdAndDateBetween(groupId, start, end);
        if (lessons.isEmpty()) return new GroupMonthlyReportResponse(Collections.emptyList(), Collections.emptyList());

        List<Long> lessonIds = lessons.stream().map(Lesson::getId).toList();
        List<Long> studentIds = students.stream().map(StudentProfile::getId).toList();

        List<Integer> lessonDays = lessons.stream()
                .map(l -> l.getDate().getDayOfMonth())
                .distinct()
                .sorted()
                .toList();

        List<Attendance> attendances = attendanceRepository.findByLessonIdInAndStudentIdIn(lessonIds, studentIds);
        List<Grade> grades = gradeRepository.findByLessonIdInAndStudentIdIn(lessonIds, studentIds);

        Map<String, Attendance> attendanceMap = attendances.stream()
                .collect(Collectors.toMap(
                        a -> a.getLesson().getId() + "_" + a.getStudent().getId(),
                        a -> a
                ));
        Map<String, Grade> gradeMap = grades.stream()
                .collect(Collectors.toMap(
                        g -> g.getLesson().getId() + "_" + g.getStudent().getId(),
                        g -> g
                ));

        List<StudentAttendanceGradeReport> studentReports = new ArrayList<>();
        for (StudentProfile student : students) {
            StudentAttendanceGradeReport report = new StudentAttendanceGradeReport();
            report.setStudentId(student.getId());
            String fullName = student.getUser().getFirstName() + " " + student.getUser().getLastName();
            report.setFullName(fullName);

            List<AttendanceGradeInfo> agList = new ArrayList<>();
            for (Lesson lesson : lessons) {
                AttendanceGradeInfo ag = new AttendanceGradeInfo();
                ag.setLessonId(lesson.getId());
                ag.setLessonDate(lesson.getDate());
                ag.setTopic(lesson.getTopic());
                ag.setLessonStartTime(lesson.getStartTime());
                ag.setLessonEndTime(lesson.getEndTime());

                String key = lesson.getId() + "_" + student.getId();

                Attendance attendance = attendanceMap.get(key);
                if (attendance != null) {
                    ag.setPresent(attendance.isPresent());
                    ag.setAttendanceComment(attendance.getComment());
                    ag.setAttendanceId(attendance.getId());
                } else {
                    ag.setPresent(null);
                    ag.setAttendanceComment(null);
                }

                Grade grade = gradeMap.get(key);
                if (grade != null) {
                    ag.setGrade(grade.getScore());
                    ag.setGradeComment(grade.getComment());
                    ag.setGradeId(grade.getId());
                } else {
                    ag.setGrade(null);
                    ag.setGradeComment(null);
                }

                agList.add(ag);
            }
            report.setAttendanceList(agList);
            studentReports.add(report);
        }
        return new GroupMonthlyReportResponse(lessonDays, studentReports);
    }
}
