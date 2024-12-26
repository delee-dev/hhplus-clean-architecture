package io.hhplus.lecture.infrastructure.lecture;

import io.hhplus.lecture.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
    @Query("SELECT l FROM Lecture l WHERE :searchTime BETWEEN l.lectureInfo.recruitmentPeriod.startTime AND l.lectureInfo.recruitmentPeriod.endTime")
    List<Lecture> findLecturesInRecruitmentPeriod(@Param("searchTime") LocalDateTime searchTime);
}
