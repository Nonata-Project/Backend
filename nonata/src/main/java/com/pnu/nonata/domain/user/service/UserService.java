package com.pnu.nonata.domain.user.service;


import com.pnu.nonata.domain.user.repository.UserMemberRepository;
import com.pnu.nonata.global.model.Member;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserMemberRepository memberRepository;
    public boolean isUserExist(String socialID){
        Member member = memberRepository.findBySocialId(socialID)
                .orElse(null);

        if(member==null)
            return false;

        return true;
    }

    public boolean saveUser(Member member){
        try{
            memberRepository.save(member);
        }
        catch (NoResultException e){
            return false;
        }
        return true;
    }


}
