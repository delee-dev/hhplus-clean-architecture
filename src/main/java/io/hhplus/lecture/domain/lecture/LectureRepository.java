package io.hhplus.lecture.domain.lecture;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository {
    Lecture findById(long id);
    Lecture findByIdWithLock(Long id);
    List<Lecture> findLecturesInRecruitmentPeriod(LocalDateTime localDateTime);
    Lecture update(Lecture lecture);
}
