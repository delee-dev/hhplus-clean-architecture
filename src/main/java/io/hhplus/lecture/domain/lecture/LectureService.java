package io.hhplus.lecture.domain.lecture;

import io.hhplus.lecture.domain.lecture.dto.LectureDetailDto;
import io.hhplus.lecture.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {
    public final LectureRepository lectureRepository;

    @Transactional(readOnly = true)
    public List<LectureDetailDto> findLecturesInRecruitmentPeriod(String searchDateTime) {
        return LectureDetailDto.fromEntities(lectureRepository.findLecturesInRecruitmentPeriod(DateUtil.toLocalDateTime(searchDateTime)));
    }
}
