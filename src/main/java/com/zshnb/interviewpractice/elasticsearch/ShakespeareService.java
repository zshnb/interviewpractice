package com.zshnb.interviewpractice.elasticsearch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.searchafter.SearchAfterBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class ShakespeareService {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public ShakespeareService(
        ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public List<Shakespeare> list(ListRequest request) {
        PageRequest pageRequest = PageRequest.of(request.page, request.limit);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
            .withPageable(pageRequest);
        if (!StringUtils.isEmpty(request.textEntry)) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("text_entry", request.textEntry);
            queryBuilder.withQuery(matchQueryBuilder);
            SortBuilder sortBuilder = SortBuilders.fieldSort("line_id");
            sortBuilder.order(SortOrder.ASC);
            queryBuilder.withSort(sortBuilder);
        }

        NativeSearchQuery nativeSearchQuery = queryBuilder.build();
        if (request.lastLineId != null) {
            SearchAfterBuilder searchAfterBuilder = new SearchAfterBuilder();
            searchAfterBuilder.setSortValues(new Object[]{request.lastLineId});
            nativeSearchQuery.setSearchAfter(Arrays.asList(searchAfterBuilder.getSortValues()));
        }

        return elasticsearchRestTemplate.search(nativeSearchQuery, Shakespeare.class)
            .getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public static class ListRequest {
        int page;
        int limit;
        String textEntry;
        Long lastLineId;

        public ListRequest(int page, int limit, String textEntry) {
            this.page = page;
            this.limit = limit;
            this.textEntry = textEntry;
        }

        public static ListRequest of(int page, int limit) {
            return new ListRequest(page, limit, "");
        }

        public static ListRequest of(int page, int limit, String textEntry) {
            return new ListRequest(page, limit, textEntry);
        }

        public ListRequest setLastLineId(Long lastLineId) {
            this.lastLineId = lastLineId;
            return this;
        }
    }
}
