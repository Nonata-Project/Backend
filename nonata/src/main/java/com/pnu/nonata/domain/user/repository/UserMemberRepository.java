package com.pnu.nonata.domain.user.repository;

import com.pnu.nonata.global.model.Member;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserMemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findBySocialId(String socialId) throws NoResultException;

}
