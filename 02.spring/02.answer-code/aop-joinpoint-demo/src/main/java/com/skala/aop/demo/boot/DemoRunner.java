package com.skala.aop.demo.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.skala.aop.demo.service.MemberService;

@Component
public class DemoRunner implements CommandLineRunner {

    private final MemberService memberService;

    public DemoRunner(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== CommandLineRunner 시작 ===");
        System.out.println(memberService.getMember(42));
        memberService.updateMember(7, "bob");

        try {
            memberService.getMember(-1); // 예외 케이스 (AfterThrowing 확인용)
        } catch (Exception ignore) {}
        System.out.println("=== CommandLineRunner 종료 ===");
    }
}
