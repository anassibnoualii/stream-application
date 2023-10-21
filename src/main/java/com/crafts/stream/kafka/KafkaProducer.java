package com.crafts.stream.kafka;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void loadData(String document) {
    try {
      kafkaTemplate.send(buildProjectRecord(document));
    } catch (Exception e) {
      log.error("Error while parsing project document: {}", e.getMessage());
    }
  }

  private ProducerRecord<String, String> buildProjectRecord(String document) {
    var id = UUID.randomUUID().toString();
    return new ProducerRecord<>(
        AppTopics.STREAM,
        null,
        id,
        document,
        List.of(new RecordHeader("ID", StringUtils.getBytesUtf8(id))));
  }
}
