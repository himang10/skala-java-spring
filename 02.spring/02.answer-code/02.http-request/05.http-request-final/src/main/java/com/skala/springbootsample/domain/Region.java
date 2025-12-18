package com.skala.springbootsample.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regions")
@Getter
@Setter
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

    // 편의 생성자
    public Region(String name) {
        this.name = name;
    }

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
