package com.http200ok.finbuddy.member.repository;

import com.http200ok.finbuddy.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
