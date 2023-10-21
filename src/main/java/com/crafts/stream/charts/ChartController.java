package com.crafts.stream.charts;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartController {
  @GetMapping("/chart")
  public String chart(Model model) {
    List<Integer> chartData = Arrays.asList(10, 20, 30, 40, 50);
    List<String> chartLabels = Arrays.asList("A", "B", "C", "D", "E");

    model.addAttribute("chartData", chartData);
    model.addAttribute("chartLabels", chartLabels);

    return "chart"; // The name of your Thymeleaf template file (without the ".html" extension).
  }
}
