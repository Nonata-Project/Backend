package com.pnu.nonata.domain.room.service;

import com.pnu.nonata.domain.room.repository.UserRepository;
import com.pnu.nonata.global.model.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository UserRepository;

    public Member findUser(String socialId){
        return UserRepository.findBySocialId(socialId).orElse(null);
    }

}
