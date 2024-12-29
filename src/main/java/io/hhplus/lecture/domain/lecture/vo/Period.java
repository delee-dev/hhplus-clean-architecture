package io.hhplus.lecture.domain.lecture.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    public boolean isBefore(LocalDateTime dateTime) {
        return dateTime.isBefore(startTime);
    }

    public boolean isAfter(LocalDateTime dateTime) {
        return dateTime.isAfter(endTime);
    }
}
