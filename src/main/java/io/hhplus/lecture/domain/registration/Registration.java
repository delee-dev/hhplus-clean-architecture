package io.hhplus.lecture.domain.registration;

import io.hhplus.lecture.domain.lecture.Lecture;
import io.hhplus.lecture.domain.lecture.enums.RegistrationStatus;
import io.hhplus.lecture.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "attendee_id")
    private Member attendee;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();
        status = RegistrationStatus.COMPLETED;
    }

    public Registration(Lecture lecture, Member attendee) {
        this.attendee = attendee;
        this.lecture = lecture;
    }
}
