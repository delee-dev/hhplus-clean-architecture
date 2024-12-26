package io.hhplus.lecture.domain.lecture.dto;

import io.hhplus.lecture.domain.lecture.Lecture;
import io.hhplus.lecture.domain.lecture.enums.LectureStatus;
import io.hhplus.lecture.domain.registration.enums.RecruitmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record LectureDetailDto(
        long id,
        String title,
        String lecturerName,
        int maxAttendees,
        int currentAttendees,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LectureStatus lectureStatus,
        LocalDateTime recruitmentStartTime,
        LocalDateTime recruitmentEndTime,
        RecruitmentStatus recruitmentStatus
) {
    public static LectureDetailDto fromEntity(Lecture lecture) {
        long id = lecture.getId();
        String title = lecture.getLectureInfo().getTitle();
        String lecturerName = lecture.getLectureInfo().getLecturer().getName();
        int maxAttendees = lecture.getMaxCapacity();
        int currentAttendees = lecture.getCurrentCapacity();
        LocalDateTime startTime = lecture.getLectureInfo().getLecturePeriod().getStartTime();
        LocalDateTime endTime = lecture.getLectureInfo().getLecturePeriod().getEndTime();
        LectureStatus lectureStatus = lecture.getLectureInfo().getLectureStatus();
        LocalDateTime recruitmentStartTime = lecture.getLectureInfo().getRecruitmentPeriod().getStartTime();
        LocalDateTime recruitmentEndTime = lecture.getLectureInfo().getRecruitmentPeriod().getEndTime();
        RecruitmentStatus recruitmentStatus = lecture.getLectureInfo().getRecruitmentStatus();

        return new LectureDetailDto(id, title, lecturerName, maxAttendees, currentAttendees, startTime, endTime, lectureStatus, recruitmentStartTime, recruitmentEndTime, recruitmentStatus);
    }

    public static List<LectureDetailDto> fromEntities(List<Lecture> lectureList) {
        return lectureList.stream().map(LectureDetailDto::fromEntity).toList();

    }
}
