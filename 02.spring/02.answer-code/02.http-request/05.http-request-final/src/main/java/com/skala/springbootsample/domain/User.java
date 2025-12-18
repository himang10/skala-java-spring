package com.skala.springbootsample.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // Region과의 Many-to-One 관계 설정
    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    // Region을 제외한 생성자 (편의용)
    public User(String name, String email, Region region) {
        this.name = name;
        this.email = email;
        this.region = region;
    }
}
