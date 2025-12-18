package com.skala.examples.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Positive
  private long id;
  
  @NotBlank
  private String name;

  @Min(1)
  @Max(99)
  private Integer age;
  
  @Email
  private String email;
}

