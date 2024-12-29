package io.hhplus.lecture.controller.registration.request;

public record RegisterRequest (
    long attendeeId,
    long lectureId
){}
