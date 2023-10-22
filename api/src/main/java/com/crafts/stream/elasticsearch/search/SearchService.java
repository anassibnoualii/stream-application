package com.crafts.stream.elasticsearch.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.crafts.stream.elasticsearch.search.KeyValue.KeyValueBuilder;
import com.crafts.stream.elasticsearch.search.SearchMetric.SearchMetricBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

  private final ElasticsearchClient esClient;

  public List<SearchMetric> streams(SearchFilter filter) throws IOException {
    Map<String, Aggregate> aggregations = performSearch(filter);
    log.info(aggregations.toString());

    return aggregations.entrySet().stream().map(this::mapAggregateToSearchMetric).toList();
  }

  private Map<String, Aggregate> performSearch(SearchFilter filter) throws IOException {
    return esClient
        .search(s -> s.index(filter.getIndex()).aggregations(streamAggregations()), Void.class)
        .aggregations();
  }

  private SearchMetric mapAggregateToSearchMetric(Map.Entry<String, Aggregate> entry) {
    String key = entry.getKey();
    Aggregate value = entry.getValue();
    List<KeyValue> metric = createKeyValueList(value);
    log.info("key: {}", key);
    log.info("values: {}", metric);
    return new SearchMetricBuilder().key(key).metrics(metric).build();
  }

  private List<KeyValue> createKeyValueList(Aggregate value) {

    if (value.isLterms()) {
      return value.lterms().buckets().array().stream()
          .map(
              bucket ->
                  new KeyValueBuilder()
                      .key(bucket.keyAsString())
                      .value(String.valueOf(bucket.docCount()))
                      .build())
          .toList();
    }
    return value.sterms().buckets().array().stream()
        .map(
            bucket ->
                new KeyValueBuilder()
                    .key(bucket.key().stringValue())
                    .value(String.valueOf(bucket.docCount()))
                    .build())
        .toList();
  }

  private Map<String, Aggregation> streamAggregations() {
    Aggregation a = new Aggregation.Builder().terms(b -> b.field("type.keyword")).build();
    Aggregation m = new Aggregation.Builder().terms(b -> b.field("meta.domain.keyword")).build();
    Aggregation r = new Aggregation.Builder().terms(b -> b.field("bot")).build();
    return Map.of("Changes by type", a, "Changes by domain", m, "Changes by bot", r);
  }

  public void createIndex(String indexName) throws IOException {
    var indexExists = esClient.indices().exists(ExistsRequest.of(e -> e.index(indexName)));
    if (indexExists.value()) {
      log.info(String.format("Index %s already exists", indexName));
    } else {
      var createIndexResponse =
          esClient.indices().create(createIndexBuilder -> createIndexBuilder.index(indexName));
      log.info(String.format("Index %s created", createIndexResponse.index()));
    }
  }
  public void createOrUpdateStream(Object document, String index) throws IOException {
    IndexResponse response =
        esClient.index(i -> i.index(index).document(document));
    if (response.result().name().equals("Created")) {
      log.info("Document has been successfully created.");
    } else if (response.result().name().equals("Updated")) {
      log.info("Document has been successfully updated.");
    } else {
      log.error("Error while performing the operation.");
    }
  }

}
