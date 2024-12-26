package io.hhplus.lecture.infrastructure.registration;

import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.registration.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface RegistrationJpaRepository extends JpaRepository<Registration, Long> {
    @Query("SELECT r FROM Registration r WHERE r.attendee.id = :attendeeId AND r.status = :status AND r.lecture.lectureInfo.lecturePeriod.startTime > :startTime")
    List<Registration> findByAttendeeIdAndStatusAndLectureStartTimeBefore(Long attendeeId, RegistrationStatus status, LocalDateTime startTime);
    List<Registration> findByLectureIdAndStatus(Long lectureId, RegistrationStatus status);
}
