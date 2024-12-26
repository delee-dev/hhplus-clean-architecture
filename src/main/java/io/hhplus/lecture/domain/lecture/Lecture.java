package io.hhplus.lecture.domain.lecture;

import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.lecture.vo.LectureInfo;
import io.hhplus.lecture.domain.registration.Registration;
import io.hhplus.lecture.domain.registration.enums.RecruitmentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private LectureInfo lectureInfo;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "current_capacity")
    private int currentCapacity;

    @OneToMany(mappedBy = "lecture")
    List<Registration> registrations = new ArrayList<>();

    public void validateLectureRegistration(long attendeeId) {
        validateRecruitmentOpen();
        validateLectureCapacity();
        validateDuplicatedRegistration(attendeeId);
    }

    private void validateRecruitmentOpen() {
        if (lectureInfo.getRecruitmentStatus() != RecruitmentStatus.OPEN) {
            throw new IllegalStateException("선택한 강의는 현재 모집 중인 상태가 아닙니다.");
        }
    }

    private void validateLectureCapacity() {
        if (maxCapacity <= currentCapacity) {
            throw new IllegalStateException("선택한 강의의 정원이 모두 찼습니다. 더 이상 등록할 수 없습니다.");
        }
    }

    private void validateDuplicatedRegistration(long attendeeId) {
        registrations.stream()
                .filter(registration -> registration.getAttendee().getId().equals(attendeeId))
                .filter(registration -> registration.getStatus().equals(RegistrationStatus.COMPLETED))
                .findFirst()
                .ifPresent(registration -> {
                    throw new IllegalStateException("선택한 강의에 중복되는 신청이 존재합니다. 동일 특강을 중복하여 신청할 수 없습니다.");
                });
    }

    public void incrementCurrentCapacity() {
        currentCapacity++;
    }

    public Lecture(LectureInfo lectureInfo, int maxCapacity) {
        this.lectureInfo = lectureInfo;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }
}
