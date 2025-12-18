package com.skala.springbootsample.domain;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Table(name = "regions")
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    public Region(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}

