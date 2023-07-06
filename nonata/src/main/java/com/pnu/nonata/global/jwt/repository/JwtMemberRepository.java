package com.pnu.nonata.global.jwt.repository;


import com.pnu.nonata.global.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtMemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findBySocialId(String socialId);

}
