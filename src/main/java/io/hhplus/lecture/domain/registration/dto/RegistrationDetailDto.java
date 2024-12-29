package io.hhplus.lecture.domain.registration.dto;

import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.registration.Registration;

import java.util.List;

public record RegistrationDetailDto (
        long attendeeId,
        long lectureId,
        String title,
        RegistrationStatus status
) {
    public static RegistrationDetailDto fromEntity(Registration registration) {
        long attendeeId = registration.getAttendee().getId();
        long lectureId = registration.getLecture().getId();
        String title = registration.getLecture().getLectureInfo().getTitle();
        RegistrationStatus status = registration.getStatus();

        return new RegistrationDetailDto(attendeeId, lectureId, title, status);
    }

    public static List<RegistrationDetailDto> fromEntities(List<Registration> registrations) {
        return registrations.stream().map(RegistrationDetailDto::fromEntity).toList();
    }
}
