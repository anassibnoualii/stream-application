package com.crafts.stream.charts;

import com.crafts.stream.elasticsearch.search.SearchFilter;
import com.crafts.stream.elasticsearch.search.SearchService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ChartController {
  private final SearchService searchService;

  public ChartController(SearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping("/chart")
  public String chart(Model model) throws IOException {

    SearchFilter filter = new SearchFilter();
    filter.setIndex("stream");
    model.addAttribute("chartData", searchService.streams(filter));

    return "chart";
  }
}
