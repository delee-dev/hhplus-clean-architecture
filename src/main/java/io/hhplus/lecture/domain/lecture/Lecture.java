package io.hhplus.lecture.domain.lecture;

import io.hhplus.lecture.domain.lecture.vo.LectureInfo;
import io.hhplus.lecture.domain.registration.Registration;
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

    public Lecture(LectureInfo lectureInfo, int maxCapacity) {
        this.lectureInfo = lectureInfo;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
    }
}
