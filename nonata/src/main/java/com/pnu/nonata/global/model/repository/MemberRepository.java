package com.pnu.nonata.global.model.repository;

import com.pnu.nonata.global.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findBySocialId(String socialId);
    Optional<Member> findByRefreshToken(String refreshToken);
}
