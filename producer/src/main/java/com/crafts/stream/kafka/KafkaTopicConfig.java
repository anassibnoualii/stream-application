package com.crafts.stream.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfig {

  @Bean
  public NewTopic stream() {
    return TopicBuilder.name(AppTopics.STREAM).build();
  }
}
