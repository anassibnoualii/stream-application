package com.crafts.stream.elasticsearch.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.util.ObjectBuilder;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

  private final ElasticsearchClient esClient;

  private static Double getAggregateValue(Entry<String, Aggregate> entry) {
    if (entry.getValue().isValueCount()) {
      return entry.getValue().valueCount().value();
    }
    if (entry.getValue().isSum()) {
      return entry.getValue().sum().value();
    }
    if (entry.getValue().isMax()) {
      return entry.getValue().max().value();
    } else return null;
  }

  private static Function<Builder, ObjectBuilder<Query>> lastDateQuery(SearchFilter filter) {
    return m ->
        m.range(
            r -> {
              r.field("lastModifiedDate");
              if (filter.getFormat() != null) {
                r.format(filter.getFormat());
              }
              if (filter.getStartDate() != null) {
                r.from(filter.getStartDate());
              }
              if (filter.getEndDate() != null) {
                r.to(filter.getEndDate());
              }
              return r;
            });
  }

  private static Function<Builder, ObjectBuilder<Query>> tenantQuery(String tenant) {
    return m -> m.term(t -> t.field("tenant").value(tenant));
  }

  private static Function<Builder, ObjectBuilder<Query>> fieldQuery(SearchFilter searchFilter) {
    return m -> m.term(t -> t.field(searchFilter.getField()).value(searchFilter.getFieldValue()));
  }

  public Object scenarios(SearchFilter filter) throws IOException {

    return esClient
        .search(
            s ->
                s.index(filter.getIndex())
                    .query(
                        q ->
                            q.bool(
                                b ->
                                    b.must(tenantQuery(filter.getTenant()))
                                        .must(lastDateQuery(filter))
                                        .must(fieldQuery(filter))))
                    .aggregations(scenarioMetrics()),
            Void.class)
        .aggregations()
        .entrySet()
        .stream()
        .map(this::mapMetricToValue)
        .toList();
  }

  private Map<String, Aggregation> scenarioMetrics() {
    Aggregation a = new Aggregation.Builder().sum(b -> b.field("automated")).build();
    Aggregation m = new Aggregation.Builder().sum(b -> b.field("manual")).build();
    Aggregation r = new Aggregation.Builder().sum(b -> b.field("requirement")).build();
    Aggregation t = new Aggregation.Builder().valueCount(b -> b.field("id")).build();
    return Map.of("automated", a, "manual", m, "requirement", r, "tests", t);
  }

  private KeyValueBase mapMetricToValue(Entry<String, Aggregate> aggregateEntry) {
    return KeyValueBase.builder()
        .key(aggregateEntry.getKey())
        .value(getAggregateValue(aggregateEntry))
        .build();
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
