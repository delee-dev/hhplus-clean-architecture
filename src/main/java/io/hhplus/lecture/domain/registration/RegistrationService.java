package io.hhplus.lecture.domain.registration;

import io.hhplus.lecture.domain.lecture.Lecture;
import io.hhplus.lecture.domain.lecture.LectureRepository;
import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.member.Member;
import io.hhplus.lecture.domain.member.MemberRepository;
import io.hhplus.lecture.domain.registration.dto.RegistrationDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RegistrationDetailDto register(long lectureId, long attendeeId) {
        Lecture lecture = lectureRepository.findById(lectureId);
        Member attendee = memberRepository.findById(attendeeId);

        // validation
        lecture.validateLectureRegistration(attendeeId);

        // save registration
        Registration registration = new Registration(lecture, attendee);
        registrationRepository.save(registration);

        // update lecture
        lecture.incrementCurrentCapacity();
        lectureRepository.update(lecture);

        return RegistrationDetailDto.fromEntity(registration);
    }

    @Transactional(readOnly = true)
    public List<RegistrationDetailDto> findUpcomingRegistrations(long attendeeId) {
        return RegistrationDetailDto.fromEntities(registrationRepository.findByAttendeeIdAndStatusAndLectureStartTimeBefore(attendeeId, RegistrationStatus.COMPLETED, LocalDateTime.now()));
    }
}
