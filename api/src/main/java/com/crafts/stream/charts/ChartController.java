package com.crafts.stream.charts;

import com.crafts.stream.elasticsearch.search.SearchFilter;
import com.crafts.stream.elasticsearch.search.SearchMetric;
import com.crafts.stream.elasticsearch.search.SearchService;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class ChartController {
  private final SearchService searchService;

  public ChartController(SearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping("/chart")
  public ResponseEntity<List<SearchMetric>> chart(Model model) throws IOException {

    SearchFilter filter = new SearchFilter();
    filter.setIndex("stream");

    return new ResponseEntity<>(searchService.streams(filter), HttpStatus.OK);
  }
}
