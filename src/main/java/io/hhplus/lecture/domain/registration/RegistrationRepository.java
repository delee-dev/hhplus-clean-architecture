package io.hhplus.lecture.domain.registration;

import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistrationRepository {
    List<Registration> findByAttendeeIdAndStatusAndLectureStartTimeBefore(long attendeeId, RegistrationStatus status, LocalDateTime searchTime);
    List<Registration> findByLectureIdAndStatus(long lectureId, RegistrationStatus status);
    Registration save(Registration registration);
}
