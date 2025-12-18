package com.skala.example.domain;

import java.util.List;

public class User {
  private long id;
  private String name;
  private String email;
  private List<String> hobbies; // 취미


  // 생성자, getter 및 setter
  public User(long id, String name, String email, List<String> hobbies) {
      this.id = id;
      this.name = name;
      this.email = email;
      this.hobbies = hobbies;
  }

  public long getId() {
      return id;
  }

  public void setId(long id) {
      this.id = id;
  }

  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public String getEmail() {
      return email;
  }

  public void setEmail(String email) {
      this.email = email;
  }

  public List<String> getHobbies() {
      return hobbies;
  }

  public void setHobbies(List<String> hobbies) {
      this.hobbies = hobbies;
  }
}

