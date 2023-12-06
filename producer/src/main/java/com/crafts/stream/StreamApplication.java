package com.crafts.stream;

import com.crafts.stream.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
@Slf4j
public class StreamApplication implements CommandLineRunner {
  private final WebClient webClient;
  private final KafkaProducer kafkaProducer;

  public StreamApplication(KafkaProducer kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
    this.webClient = WebClient.builder().baseUrl("https://stream.wikimedia.org/v2/stream/recentchange").build();
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
  }

  public Flux<String> getStreamData() {
    return webClient.get().retrieve().bodyToFlux(String.class)
        .delayElements(Duration.ofMillis(100))
        .doOnError(Throwable::printStackTrace)
        .retry();
  }
}
