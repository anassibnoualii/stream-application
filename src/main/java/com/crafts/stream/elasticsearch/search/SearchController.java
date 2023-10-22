package com.crafts.stream.elasticsearch.search;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SearchController {

  private final SearchService searchService;

  @PostMapping("/search")
  public ResponseEntity<Object> scenarios(@RequestBody SearchFilter filter) throws IOException {
    return new ResponseEntity<>(searchService.streams(filter), HttpStatus.OK);
  }
}
