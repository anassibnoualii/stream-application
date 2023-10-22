package com.crafts.stream.kafka;

import com.crafts.stream.elasticsearch.search.SearchService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

  private final SearchService searchService;

  public KafkaConsumer(SearchService searchService) {
    this.searchService = searchService;
  }

  @KafkaListener(topics = AppTopics.STREAM)
  public void onMessageReceived(
      @Header("ID") String id, @Payload String message, Acknowledgment acknowledgment)
      throws IOException {
    log.info(message);
    log.info(id);
    ObjectMapper objectMapper = new ObjectMapper();
    TypeReference<Object> mapType = new TypeReference<>() {};
    Object payload = objectMapper.readValue(message, mapType);
    log.info("Data in consumer: {}", payload);
    searchService.createOrUpdateStream(payload, "stream");
    acknowledgment.acknowledge();
  }
}
