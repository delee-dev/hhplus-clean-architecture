package io.hhplus.lecture.infrastructure.member;

import io.hhplus.lecture.domain.member.Member;
import io.hhplus.lecture.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class MemberJpaCustomRepository implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member findById(Long id) {
        return memberJpaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 id를 가진 유저가 존재하지 않습니다."));
    }
}
