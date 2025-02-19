package com.http200ok.finbuddy.member.domain;

import com.http200ok.finbuddy.account.domain.Account;
import com.http200ok.finbuddy.budget.domain.Budget;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String nickname;
    private String phone;
    private String simplePassword;
    private String address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Budget> budgets = new ArrayList<>();

}
