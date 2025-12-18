package com.skala.springbootsample.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Table(name = "regions")
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // User와의 One-to-Many 관계 설정
    @OneToMany(mappedBy = "region")
    @JsonIgnore
    private List<User> users = new ArrayList<>();


    // 연관관계 편의 메서드
    public void addUser(User user) {
        users.add(user);
        user.setRegion(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setRegion(null);
    }
}

