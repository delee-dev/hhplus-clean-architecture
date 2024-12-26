package io.hhplus.lecture.domain.lecture.vo;

import io.hhplus.lecture.domain.lecture.enums.LectureStatus;
import io.hhplus.lecture.domain.registration.enums.RecruitmentStatus;
import io.hhplus.lecture.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureInfo {
    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Member lecturer;

    @Embedded
    private Period lecturePeriod;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startTime", column = @Column(name = "recruitment_start_time")),
            @AttributeOverride(name = "endTime", column = @Column(name = "recruitment_end_time"))
    })
    private Period recruitmentPeriod;

    @Transient
    private LectureStatus lectureStatus;

    @Transient
    private RecruitmentStatus recruitmentStatus;

    @PostLoad
    private void onRetrieve() {
        LocalDateTime now = LocalDateTime.now();

        calculateLectureStatus(now);
        calculateRecruitmentStatus(now);
    }

    private void calculateLectureStatus(LocalDateTime baseDateTime) {
        if (lecturePeriod.isBefore(baseDateTime)) {
            this.lectureStatus = LectureStatus.UPCOMING;
        } else if (lecturePeriod.isAfter(baseDateTime)) {
            this.lectureStatus = LectureStatus.FINISHED;
        } else {
            this.lectureStatus = LectureStatus.IN_PROGRESS;
        }
    }

    private void calculateRecruitmentStatus(LocalDateTime baseDateTime) {
        if (recruitmentPeriod.isBefore(baseDateTime)) {
            this.recruitmentStatus = RecruitmentStatus.PENDING;
        } else if (recruitmentPeriod.isAfter(baseDateTime)) {
            this.recruitmentStatus = RecruitmentStatus.CLOSED;
        } else {
            this.recruitmentStatus = RecruitmentStatus.OPEN;
        }
    }

    public LectureInfo(String title, Member lecturer, Period lecturePeriod, Period recruitmentPeriod) {
        this.title = title;
        this.lecturer = lecturer;
        this.lecturePeriod = lecturePeriod;
        this.recruitmentPeriod = recruitmentPeriod;

        calculateLectureStatus(LocalDateTime.now());
        calculateRecruitmentStatus(LocalDateTime.now());
    }
}
