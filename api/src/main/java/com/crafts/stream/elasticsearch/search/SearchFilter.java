package com.crafts.stream.elasticsearch.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchFilter {
  private String index;
  private String tenant;
  private String field;
  private String fieldValue;
  private String startDate;
  private String endDate;
  private String format;
}
