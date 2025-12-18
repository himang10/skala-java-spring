package com.skala.basic.table;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String topic; // 여러 개의 주제가 필요하다면 콤마(,)로 구분된 문자열로 저장

  private String description;
}
