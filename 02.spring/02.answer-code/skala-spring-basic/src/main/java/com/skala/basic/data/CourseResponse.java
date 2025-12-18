package com.skala.basic.data;

import lombok.Data;
import java.util.List;

@Data
public class CourseResponse {
  private String name;
  private List<String> topics;
  private String description;
}
