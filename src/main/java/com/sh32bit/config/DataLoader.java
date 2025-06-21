package com.sh32bit.config;

import com.sh32bit.entity.*;
import com.sh32bit.enums.GroupStatus;
import com.sh32bit.enums.Role;
import com.sh32bit.repository.*;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private final Faker faker = new Faker();

    @Override
    public void run(String... args) {
        // 1. Users
        List<User> userList = generateUsers();
        userRepository.saveAll(userList);

        // 2. Student Profiles
        List<StudentProfile> studentProfiles = generateStudentProfiles(userList);
        studentProfileRepository.saveAll(studentProfiles);

        // 3. Parent Profiles
        List<ParentProfile> parentProfiles = generateParentProfiles(userList);
        parentProfileRepository.saveAll(parentProfiles);

        // 4. Teacher Profiles
        List<TeacherProfile> teacherProfiles = generateTeacherProfiles(userList);
        teacherProfileRepository.saveAll(teacherProfiles);

        // 5. Courses
        List<Course> courseList = generateCourses();
        courseRepository.saveAll(courseList);

        // 6. Groups
        List<Group> groupList = generateGroups(courseList);
        groupRepository.saveAll(groupList);

        // 7. GroupTeachers
        List<GroupTeacher> groupTeachers = generateGroupTeachers(groupList, teacherProfiles);
        groupTeacherRepository.saveAll(groupTeachers);

        // 8. GroupStudents
        List<GroupStudent> groupStudents = generateGroupStudents(groupList, studentProfiles);
        groupStudentRepository.saveAll(groupStudents);

        // 9. Lessons
        List<Lesson> lessons = lessonGenerator(groupList, groupTeachers);
        lessonRepository.saveAll(lessons);

        // 10. Attendance
        List<Attendance> attendances = generateAttendance(studentProfiles, lessons);
        attendanceRepository.saveAll(attendances);

        // 11. Grades
        List<Grade> grades = generateGrades(studentProfiles, lessons, attendances);
        gradeRepository.saveAll(grades);
    }

    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        Role[] roles = Role.values();
        for (int i = 0; i < 20; i++) {
            users.add(User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .role(roles[faker.number().numberBetween(0, roles.length)])
                    .enabled(faker.bool().bool())
                    .password(passwordEncoder.encode("123456"))
                    .build());
        }
        return users;
    }

    private List<StudentProfile> generateStudentProfiles(List<User> userList) {
        List<StudentProfile> students = new ArrayList<>();

        userList.stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .forEach(u -> students.add(StudentProfile.builder().user(u).build()));
        return students;
    }

    private List<ParentProfile> generateParentProfiles(List<User> userList) {
        List<ParentProfile> parents = new ArrayList<>();
        userList.stream()
                .filter(u -> u.getRole() == Role.PARENT)
                .forEach(u -> parents.add(ParentProfile.builder().user(u).build()));
        return parents;
    }

    private List<TeacherProfile> generateTeacherProfiles(List<User> userList) {
        List<TeacherProfile> teachers = new ArrayList<>();
        userList.stream()
                .filter(u -> u.getRole() == Role.TEACHER)
                .forEach(u -> teachers.add(TeacherProfile.builder().user(u).build()));
        return teachers;
    }

    private List<Course> generateCourses() {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            courses.add(Course.builder()
                    .name(faker.educator().course())
                    .price(BigDecimal.valueOf(faker.number().numberBetween(500_000, 2_000_000)))
                    .description(faker.lorem().sentence())
                    .level(faker.options().option("Beginner", "Intermediate", "Advanced"))
                    .durationMonth(faker.number().numberBetween(2, 10))
                    .build());
        }
        return courses;
    }

    private List<Group> generateGroups(List<Course> courseList) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Course course = courseList.get(faker.number().numberBetween(0, courseList.size()));
            groups.add(Group.builder()
                    .name(faker.book().title() + " " + faker.number().randomDigitNotZero())
                    .course(course)
                    .startDateTime(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 60)))
                    .endDateTime(LocalDateTime.now().plusMonths(course.getDurationMonth()))
                    .status(GroupStatus.ACTIVE)
                    .build());
        }
        return groups;
    }

    private List<GroupTeacher> generateGroupTeachers(List<Group> groupList, List<TeacherProfile> teacherProfiles) {
        List<GroupTeacher> groupTeachers = new ArrayList<>();
        for (Group group : groupList) {
            int teacherCount = faker.number().numberBetween(1, Math.min(teacherProfiles.size(), 3));
            Set<TeacherProfile> assigned = new HashSet<>();
            for (int i = 0; i < teacherCount; i++) {
                TeacherProfile teacher = teacherProfiles.get(faker.number().numberBetween(0, teacherProfiles.size()));
                if (!assigned.contains(teacher)) {
                    groupTeachers.add(GroupTeacher.builder()
                            .group(group)
                            .teacher(teacher)
                            .joinDate(LocalDate.now().minusDays(faker.number().numberBetween(0, 100)))
                            .leaveDate(faker.bool().bool() ? LocalDate.now().
                                    minusDays(faker.number().numberBetween(1, 10)) : null)
                            .build());
                    assigned.add(teacher);
                }
            }
        }
        return groupTeachers;
    }

    private List<GroupStudent> generateGroupStudents(List<Group> groupList, List<StudentProfile> studentProfiles) {
        List<GroupStudent> groupStudents = new ArrayList<>();
        for (Group group : groupList) {
            int studentCount = faker.number().numberBetween(3, Math.min(8, studentProfiles.size()));
            Set<StudentProfile> assigned = new HashSet<>();
            for (int i = 0; i < studentCount; i++) {
                StudentProfile student = studentProfiles.get(faker.number().numberBetween(0, studentProfiles.size()));
                if (!assigned.contains(student)) {
                    groupStudents.add(GroupStudent.builder()
                            .group(group)
                            .student(student)
                            .joinDate(LocalDate.now().minusDays(faker.number().numberBetween(0, 100)))
                            .build());
                    assigned.add(student);
                }
            }
        }
        return groupStudents;
    }

    private List<Lesson> lessonGenerator(List<Group> groupList, List<GroupTeacher> groupTeachers) {
        List<Lesson> lessons = new ArrayList<>();
        DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY};

        for (Group group : groupList) {
            LocalDate start = group.getStartDateTime().toLocalDate();
            LocalDate end = group.getEndDateTime().toLocalDate();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                if (Arrays.asList(days).contains(date.getDayOfWeek())) {
                    Lesson lesson = new Lesson();
                    lesson.setDate(date);
                    lesson.setStartTime(LocalTime.of(faker.number().numberBetween(8, 18), 0));
                    lesson.setEndTime(lesson.getStartTime().plusMinutes(90));
                    lesson.setTopic(faker.educator().course() + " lesson");
                    lesson.setGroupTeacher(groupTeachers.get(faker.number().numberBetween(0, groupTeachers.size())));
                    lesson.setGroup(group);
                    lessons.add(lesson);
                }
            }
        }
        return lessons;
    }

    private List<Attendance> generateAttendance(List<StudentProfile> students, List<Lesson> lessons) {
        List<Attendance> attendances = new ArrayList<>();
        for (Lesson lesson : lessons) {
            for (StudentProfile student : students) {
                Attendance att = new Attendance();
                att.setLesson(lesson);
                att.setStudent(student);
                boolean present = faker.bool().bool();
                att.setPresent(present);
                att.setComment(present ? faker.lorem().word() : faker.lorem().sentence());
                attendances.add(att);
            }
        }
        return attendances;
    }

    private List<Grade> generateGrades(List<StudentProfile> students, List<Lesson> lessons,
                                       List<Attendance> attendances) {
        List<Grade> grades = new ArrayList<>();
        for (Lesson lesson : lessons) {
            for (StudentProfile student : students) {
                Optional<Attendance> attendanceOpt = attendances.stream()
                        .filter(a -> a.getLesson().getId().equals(lesson.getId()) &&
                                a.getStudent().getId().equals(student.getId()) &&
                                a.isPresent())
                        .findFirst();
                if (attendanceOpt.isPresent()) {
                    Attendance attendance = attendanceOpt.get();
                    Grade grade = new Grade();
                    grade.setStudent(student);
                    grade.setAttendance(attendance);
                    double score = faker.number().randomDouble(1, 4, 9);
                    grade.setScore(score);
                    grade.setComment("Fake baho: " + String.format("%.1f", score));
                    grades.add(grade);
                }
            }
        }
        return grades;
    }
}
