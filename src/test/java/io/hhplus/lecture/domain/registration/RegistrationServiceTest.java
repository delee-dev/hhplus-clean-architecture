package io.hhplus.lecture.domain.registration;

import io.hhplus.lecture.domain.lecture.Lecture;
import io.hhplus.lecture.domain.lecture.LectureRepository;
import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.lecture.vo.LectureInfo;
import io.hhplus.lecture.domain.lecture.vo.Period;
import io.hhplus.lecture.domain.member.Member;
import io.hhplus.lecture.domain.member.MemberRepository;
import io.hhplus.lecture.domain.member.enums.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    @InjectMocks
    private RegistrationService registrationService;
    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private MemberRepository memberRepository;

    class Mocks {
        static final int maxCapacity = 30;

        static final long attendeeId = 1L;
        static final long lecturerId = 2L;
        static final long lectureId = 100L;

        static final Member attendee = new Member(attendeeId, "Attendee Lee", MemberRole.ATTENDEE);
        static final Member lecturer = new Member(lectureId, "Lecturer Yoo", MemberRole.LECTURER);

        static final Period pastRecruitmentPeriod = new Period(
                LocalDateTime.now().minusMonths(4),
                LocalDateTime.now().minusMonths(3)
        );

        static final Period currentRecruitmentPeriod = new Period(
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now().plusDays(7)
        );

        static final Period futureRecruitmentPeriod = new Period(
                LocalDateTime.now().plusMonths(1),
                LocalDateTime.now().plusMonths(2)
        );

        static final Period pastLecturePeriod = new Period(
                LocalDateTime.now().minusMonths(2),
                LocalDateTime.now().minusMonths(2).plusHours(2)
        );

        static final Period ongoingLecturePeriod = new Period(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1)
        );

        static final Period upcomingLecturePeriod = new Period(
                LocalDateTime.now().plusMonths(2),
                LocalDateTime.now().plusMonths(2).plusHours(2)
        );
    }

    @Nested
    @DisplayName("특강 신청")
    class RegisterTest {
        @Test
        void 모집_기간이_아닌_특강에_신청하는_경우_요청이_실패한다() {
            // given
            LectureInfo pastRecruitedLectureInfo = new LectureInfo("HangHae Plus 6", Mocks.lecturer, Mocks.pastLecturePeriod, Mocks.pastRecruitmentPeriod);
            Lecture pastRecruitedLecture = new Lecture(pastRecruitedLectureInfo, Mocks.maxCapacity);

            when(memberRepository.findById(Mocks.attendeeId))
                    .thenReturn(Mocks.attendee);
            when(lectureRepository.findByIdWithLock(Mocks.lectureId))
                    .thenReturn(pastRecruitedLecture);

            // when & then
            assertThatThrownBy(() -> registrationService.register(Mocks.lectureId, Mocks.attendeeId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("선택한 강의는 현재 모집 중인 상태가 아닙니다.");
        }

        @Test
        void 모집_정원이_마감된_특강에_신청하는_경우_요청이_실패한다() {
            // given
            LectureInfo currentRecruitingLectureInfo = new LectureInfo("HangHae Plus 8", Mocks.lecturer, Mocks.upcomingLecturePeriod, Mocks.currentRecruitmentPeriod);
            Lecture currentRecruitingLecture = new Lecture(currentRecruitingLectureInfo, Mocks.maxCapacity);

            for (int i = 0; i < Mocks.maxCapacity; i++) {
                currentRecruitingLecture.incrementCurrentCapacity(); // 모집 시뮬레이션
            }

            when(memberRepository.findById(Mocks.attendeeId))
                    .thenReturn(Mocks.attendee);
            when(lectureRepository.findByIdWithLock(Mocks.lectureId))
                    .thenReturn(currentRecruitingLecture);

            // when & then
            assertThatThrownBy(() -> registrationService.register(Mocks.lectureId, Mocks.attendeeId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("선택한 강의의 정원이 모두 찼습니다. 더 이상 등록할 수 없습니다.");
        }

        @Test
        void 이미_신청한_특강에_신청하는_경우_요청이_실패한다() {
            // given
            LectureInfo currentRecruitingLectureInfo = new LectureInfo("HangHae Plus 8", Mocks.lecturer, Mocks.upcomingLecturePeriod, Mocks.currentRecruitmentPeriod);
            Lecture hasDuplicatedRegistrationLecture = new Lecture(currentRecruitingLectureInfo, Mocks.maxCapacity);
            Registration registration = new Registration(1, Mocks.attendee, hasDuplicatedRegistrationLecture, RegistrationStatus.COMPLETED, LocalDateTime.now());
            hasDuplicatedRegistrationLecture.getRegistrations().add(registration);

            when(memberRepository.findById(Mocks.attendeeId))
                    .thenReturn(Mocks.attendee);
            when(lectureRepository.findByIdWithLock(Mocks.lectureId))
                    .thenReturn(hasDuplicatedRegistrationLecture);

            // when & then
            assertThatThrownBy(() -> registrationService.register(Mocks.lectureId, Mocks.attendeeId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("선택한 강의에 중복되는 신청이 존재합니다. 동일 특강을 중복하여 신청할 수 없습니다.");
        }

        @Test
        void 특강_신청에_성공하는_경우_Lecture의_current_capacity_값이_증가된다() {
            // given
            LectureInfo currentRecruitingLectureInfo = new LectureInfo("HangHae Plus 8", Mocks.lecturer, Mocks.upcomingLecturePeriod, Mocks.currentRecruitmentPeriod);
            Lecture currentRecruitingLecture = new Lecture(Mocks.lectureId, currentRecruitingLectureInfo, Mocks.maxCapacity, 0, new ArrayList<>());

            int currentCapacityBeforeRegister = currentRecruitingLecture.getCurrentCapacity();

            when(memberRepository.findById(Mocks.attendeeId))
                    .thenReturn(Mocks.attendee);
            when(lectureRepository.findByIdWithLock(Mocks.lectureId))
                    .thenReturn(currentRecruitingLecture);

            // when
            registrationService.register(Mocks.lectureId, Mocks.attendeeId);

            // then
            assertThat(currentRecruitingLecture.getCurrentCapacity()).isEqualTo(currentCapacityBeforeRegister + 1);
        }
    }
}
