package io.hhplus.lecture.infrastructure.lecture;

import io.hhplus.lecture.domain.lecture.Lecture;
import io.hhplus.lecture.domain.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class LectureJpaCustomRepository implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public Lecture findById(long id) {
        return lectureJpaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 id를 가진 특강이 존재하지 않습니다."));
    }

    @Override
    public Lecture findByIdWithLock(Long id) {
        return lectureJpaRepository.findLectureById(id).orElseThrow(() -> new NoSuchElementException("해당 id를 가진 특강이 존재하지 않습니다."));
    }

    @Override
    public List<Lecture> findLecturesInRecruitmentPeriod(LocalDateTime searchTime) {
        return lectureJpaRepository.findLecturesInRecruitmentPeriod(searchTime);
    }

    @Override
    public Lecture update(Lecture lecture) {
        return lectureJpaRepository.save(lecture);
    }
}
