package com.crafts.stream.elasticsearch.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class KeyValueBase {
  Double value;
  String key;

}
