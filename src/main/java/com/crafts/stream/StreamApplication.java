package com.crafts.stream;

import com.crafts.stream.elasticsearch.AppIndices;
import com.crafts.stream.elasticsearch.search.SearchService;
import com.crafts.stream.kafka.KafkaProducer;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class StreamApplication implements CommandLineRunner {
  private final SearchService searchService;
  private final WebClient webClient;
  private final KafkaProducer kafkaProducer;

  public StreamApplication(SearchService searchService, KafkaProducer kafkaProducer) {
    this.searchService = searchService;
    this.kafkaProducer = kafkaProducer;
    this.webClient =
        WebClient.builder().baseUrl("https://stream.wikimedia.org/v2/stream/recentchange").build();
  }

  public static void main(String[] args) {
    SpringApplication.run(StreamApplication.class, args);
  }

  @Override
  public void run(String... args) {
    getStreamData()
        .toStream()
        .forEach(
            s -> {
              log.info("data from stream: {}", s);
              kafkaProducer.loadData(s);
            });
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

  public Flux<String> getStreamData() {
    return webClient.get().retrieve().bodyToFlux(String.class);
  }
}
