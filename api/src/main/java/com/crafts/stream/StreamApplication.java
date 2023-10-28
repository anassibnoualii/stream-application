package com.crafts.stream;

import com.crafts.stream.elasticsearch.AppIndices;
import com.crafts.stream.elasticsearch.search.SearchService;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class StreamApplication implements CommandLineRunner {
  private final SearchService searchService;

  public StreamApplication(SearchService searchService) {
    this.searchService = searchService;
  }

  public static void main(String[] args) {
    SpringApplication.run(StreamApplication.class, args);
  }

  @Override
  public void run(String... args) {
    List.of(AppIndices.STREAM)
        .forEach(
            index -> {
              try {
                searchService.createIndex(index);
              } catch (IOException e) {
                log.error("Error while creating indices");
              }
            });
  }
}
