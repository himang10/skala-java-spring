package com.skala.aop.demo.service;

import org.springframework.stereotype.Service;

// com.example.service.MemberService
@Service
public class MemberService {
    public String getMember(long id) { return "member-" + id; }

    public void updateMember(long id, String name) {
        System.out.println("업데이트: id=" + id + ", name=" + name);
    }
}

