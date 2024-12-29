package io.hhplus.lecture.controller.lecture;

import io.hhplus.lecture.controller.lecture.request.SearchLectureRequest;
import io.hhplus.lecture.domain.lecture.LectureService;
import io.hhplus.lecture.domain.lecture.dto.LectureDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @GetMapping("/lectures")
    public List<LectureDetailDto> getLecturesInRecruitmentPeriod(@RequestBody SearchLectureRequest request) {
        return lectureService.findLecturesInRecruitmentPeriod(request.searchDateTime());
    }
}
