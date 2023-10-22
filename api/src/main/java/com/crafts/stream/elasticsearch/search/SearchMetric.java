package com.crafts.stream.elasticsearch.search;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchMetric {
  private String key;
  private List<KeyValue> metrics;
}
