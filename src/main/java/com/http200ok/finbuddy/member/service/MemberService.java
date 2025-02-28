package com.http200ok.finbuddy.member.service;

import com.http200ok.finbuddy.member.domain.Member;
import com.http200ok.finbuddy.member.dto.SignUpRequest;
import com.http200ok.finbuddy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .birthDate(request.getBirthDate())
                .sex(request.getSex())
                .job(request.getJob())
                .income(request.getIncome())
                .build();

        memberRepository.save(member);
    }
}