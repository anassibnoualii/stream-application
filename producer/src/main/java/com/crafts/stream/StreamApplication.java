package com.crafts.stream;

import com.crafts.stream.kafka.KafkaProducer;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class StreamApplication implements CommandLineRunner {
  private final WebClient webClient;
  private final KafkaProducer kafkaProducer;

  public StreamApplication(KafkaProducer kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
    this.webClient =
        WebClient.builder().baseUrl("https://stream.wikimedia.org/v2/stream/recentchange").build();
  }

  public static void main(String[] args) {
    SpringApplication.run(StreamApplication.class, args);
  }

  @Override
  public void run(String... args) {
    Flux.interval(Duration.ofSeconds(15))
        .flatMap(tick -> getStreamData())
        .doOnNext(s -> {
          log.info("data from stream: {}", s);
          kafkaProducer.loadData(s);
        })
        .subscribe();
  }

  public Flux<String> getStreamData() {
    return webClient.get().retrieve().bodyToFlux(String.class);
  }
}
