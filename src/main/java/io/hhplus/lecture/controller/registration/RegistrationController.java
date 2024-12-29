package io.hhplus.lecture.controller.registration;

import io.hhplus.lecture.controller.registration.request.SearchRegistrationRequest;
import io.hhplus.lecture.controller.registration.request.RegisterRequest;
import io.hhplus.lecture.domain.registration.RegistrationService;
import io.hhplus.lecture.domain.registration.dto.RegistrationDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public RegistrationDetailDto register(@RequestBody RegisterRequest request) {
        return registrationService.register(request.lectureId(), request.attendeeId());
    }

    @GetMapping("/registrations/upcoming")
    public List<RegistrationDetailDto> upcomingRegistrations(@RequestBody SearchRegistrationRequest request) {
        return registrationService.findUpcomingRegistrations(request.attendeeId());
    }
}
