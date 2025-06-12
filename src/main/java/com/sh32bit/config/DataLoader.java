package com.sh32bit.config;

import com.sh32bit.entity.*;
import com.sh32bit.enums.GroupStatus;
import com.sh32bit.enums.Role;
import com.sh32bit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ParentProfileRepository parentProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final GroupStudentRepository groupStudentRepository;
    private final GroupTeacherRepository groupTeacherRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> userList = generateUsers();
        userRepository.saveAll(userList);

        List<StudentProfile> studentProfiles = generateStudentProfiles(userList);
        studentProfileRepository.saveAll(studentProfiles);

        List<ParentProfile> parentProfiles = generateParentProfiles(userList);
        parentProfileRepository.saveAll(parentProfiles);

        List<TeacherProfile> teacherProfiles = generateTeacherProfiles(userList);
        teacherProfileRepository.saveAll(teacherProfiles);

        List<Course> courseList = generateCourses();
        courseRepository.saveAll(courseList);

        List<Group> groupList = generateGroups(courseList, teacherProfiles, studentProfiles);
        groupRepository.saveAll(groupList);

        List<GroupTeacher> groupTeachers = generateGroupTeachers(groupList, teacherProfiles);
        groupTeacherRepository.saveAll(groupTeachers);

        List<GroupStudent> groupStudents = generateGroupStudents(groupList, studentProfiles);
        groupStudentRepository.saveAll(groupStudents);

        List<Lesson> lessons = lessonGenerator(groupList.get(0));
        lessonRepository.saveAll(lessons);

        List<Attendance> attendances = generateAttendance(studentProfiles, lessons);
        attendanceRepository.saveAll(attendances);

        List<Grade> grades = generateGrades(studentProfiles, lessons, attendances);
        gradeRepository.saveAll(grades);
    }

    private List<GroupStudent> generateGroupStudents(List<Group> groupList, List<StudentProfile> studentProfiles) {
        return List.of(
                GroupStudent.builder()
                        .group(groupList.get(0))
                        .student(studentProfiles.get(0))
                        .joinDate(LocalDate.now())
                        .build(),
                GroupStudent.builder()
                        .group(groupList.get(0))
                        .student(studentProfiles.get(1))
                        .joinDate(LocalDate.now())
                        .build()
        );
    }

    private List<GroupTeacher> generateGroupTeachers(List<Group> groupList, List<TeacherProfile> teacherProfiles) {
        return List.of(
                GroupTeacher.builder()
                        .group(groupList.get(0))
                        .teacher(teacherProfiles.get(0))
                        .joinDate(LocalDate.now())
                        .build()
        );
    }

    private List<Group> generateGroups(List<Course> courseList,
                                       List<TeacherProfile> teacherProfiles,
                                       List<StudentProfile> studentProfiles
    ) {
        return List.of(
                Group.builder()
                        .name("IELTS 6.0 Morning")
                        .course(courseList.get(0))
                        .startDateTime(LocalDateTime.now())
                        .endDateTime(LocalDateTime.now().plusMonths(courseList.get(0).getDurationMonth()))
                        .status(GroupStatus.ACTIVE)
                        .build());
    }

    private List<Course> generateCourses() {
        return List.of(
                Course.builder()
                        .name("IELTS General")
                        .price(new BigDecimal("1200000"))
                        .description("Base course for IELTS")
                        .level("Intermediate")
                        .durationMonth(6)
                        .build(),
                Course.builder()
                        .name("Pre-IELTS")
                        .price(new BigDecimal("800000"))
                        .description("Beginning course for IELTS")
                        .level("Beginner")
                        .durationMonth(4)
                        .build()
        );
    }

    private List<TeacherProfile> generateTeacherProfiles(List<User> userList) {
        return List.of(
                TeacherProfile.builder().user(userList.get(2)).build()
        );
    }

    private List<ParentProfile> generateParentProfiles(List<User> userList) {
        return List.of(
                ParentProfile.builder()
                        .user(userList.get(1))
                        .build()
        );
    }

    private List<StudentProfile> generateStudentProfiles(List<User> userList) {
        return List.of(
                StudentProfile.builder()
                        .user(userList.get(3))
                        .build(),
                StudentProfile.builder()
                        .user(userList.get(4))
                        .build()
        );
    }

    private List<User> generateUsers() {
        return List.of(
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("johndoe@gmail.com")
                        .role(Role.ADMIN)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build(),
                User.builder()
                        .firstName("Dilshod")
                        .lastName("Jo'rayev")
                        .email("dilshod@gmail.com")
                        .role(Role.PARENT)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build(),
                User.builder()
                        .firstName("Oybek")
                        .lastName("Hamroyev")
                        .email("oybek@gmail.com")
                        .role(Role.TEACHER)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build(),
                User.builder()
                        .firstName("Temur")
                        .lastName("G'aniyev")
                        .email("temur@gmail.com")
                        .role(Role.STUDENT)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build(),
                User.builder()
                        .firstName("Javohir")
                        .lastName("Jamolov")
                        .email("javohir@gmail.com")
                        .role(Role.STUDENT)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build()
        );
    }

    private List<Grade> generateGrades(List<StudentProfile> students,
                                       List<Lesson> lessons, List<Attendance> attendances) {
        List<Grade> grades = new ArrayList<>();
        Random random = new Random();

        for (Lesson lesson : lessons) {
            for (StudentProfile student : students) {
                Optional<Attendance> attendanceOpt = attendances.stream()
                        .filter(a ->
                                a.getLesson().getId().equals(lesson.getId())
                                        && a.getStudent().getId().equals(student.getId())
                                        && a.isPresent())
                        .findFirst();

                if (attendanceOpt.isPresent()) {
                    Grade grade = new Grade();
                    grade.setLesson(lesson);
                    grade.setStudent(student);

                    double score = 6.0 + (random.nextDouble() * 3.0);
                    grade.setScore(score);
                    grade.setComment("Fake baho: " + String.format("%.1f", score));

                    grades.add(grade);
                }
            }
        }
        return grades;
    }

    private List<Attendance> generateAttendance(List<StudentProfile> students, List<Lesson> lessons) {
        List<Attendance> attendances = new ArrayList<>();
        Random random = new Random();

        for (Lesson lesson : lessons) {
            for (StudentProfile student : students) {
                Attendance att = new Attendance();
                att.setLesson(lesson);
                att.setStudent(student);

                boolean present = random.nextBoolean();
                att.setPresent(present);
                att.setComment(present ? "yes" : "not");

                attendances.add(att);
            }
        }

        return attendances;
    }

    private List<Lesson> lessonGenerator(Group group) {
        Set<DayOfWeek> dayOfWeeks = Set.of(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY
        );

        LocalDate start = group.getStartDateTime().toLocalDate();
        LocalDate end = group.getEndDateTime().toLocalDate();

        YearMonth month = YearMonth.of(start.getYear(), start.getMonth());
        LocalDate monthStart = month.atDay(1);
        LocalDate monthEnd = month.atEndOfMonth();

        LocalDate from = start.isAfter(monthStart) ? start : monthStart;
        LocalDate to = end.isBefore(monthEnd) ? end : monthEnd;

        List<Lesson> lessons = new ArrayList<>();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            if (dayOfWeeks.contains(date.getDayOfWeek())) {
                Lesson lesson = new Lesson();
                lesson.setGroup(group);
                lesson.setDate(date);
                lesson.setStartTime(LocalTime.of(9, 0));
                lesson.setEndTime(LocalTime.of(10, 30));
                lesson.setTopic("Auto-generated lesson for " + date);
                lessons.add(lesson);
            }
        }

        return lessons;
    }
}
