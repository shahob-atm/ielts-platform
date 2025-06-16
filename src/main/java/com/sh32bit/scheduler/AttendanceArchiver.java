package com.sh32bit.scheduler;

import com.sh32bit.entity.Attendance;
import com.sh32bit.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AttendanceArchiver {
    private final AttendanceRepository attendanceRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void archiveOldAttendances() {
        LocalDate now = LocalDate.now();

        List<Attendance> attendancesToArchive = attendanceRepository.findOldAttendances(now);

        for (Attendance att : attendancesToArchive) {
            att.setArchived(true);
        }

        attendanceRepository.saveAll(attendancesToArchive);
        log.info("Archived {} attendances", attendancesToArchive.size());
    }
}
