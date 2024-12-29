package io.hhplus.lecture.infrastructure.registration;

import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.registration.Registration;
import io.hhplus.lecture.domain.registration.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RegistrationJpaCustomRepository implements RegistrationRepository {
    private final RegistrationJpaRepository registrationJpaRepository;

    @Override
    public List<Registration> findByAttendeeIdAndStatusAndLectureStartTimeBefore(long attendeeId, RegistrationStatus status, LocalDateTime searchTime) {
        return registrationJpaRepository.findByAttendeeIdAndStatusAndLectureStartTimeBefore(attendeeId, status, LocalDateTime.now());
    }

    @Override
    public List<Registration> findByLectureIdAndStatus(long lectureId, RegistrationStatus status) {
        return registrationJpaRepository.findByLectureIdAndStatus(lectureId, status);
    }

    @Override
    public Registration save(Registration registration) {
        return registrationJpaRepository.save(registration);
    }
}
