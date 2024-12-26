package io.hhplus.lecture.infrastructure.member;

import io.hhplus.lecture.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
