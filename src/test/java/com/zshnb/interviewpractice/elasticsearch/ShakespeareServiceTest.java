package com.zshnb.interviewpractice.elasticsearch;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.zshnb.interviewpractice.BaseTest;
import com.zshnb.interviewpractice.elasticsearch.ShakespeareService.ListRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.IndexOptions;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

public class ShakespeareServiceTest extends BaseTest {
    @Autowired
    private ShakespeareService shakespeareService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void beforeSetup() {
        super.beforeSetup();
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(User.class);
        if (indexOperations.exists()) {
            indexOperations.delete();
            indexOperations.createWithMapping();
        }
        List<IndexQuery> indexQueries = IntStream.range(0, 1000)
            .mapToObj(it -> {
                User user = new User(it, String.format("name-%d", it));
                return new IndexQueryBuilder()
                    .withObject(user).build();
            })
            .collect(Collectors.toList());
        elasticsearchRestTemplate.bulkIndex(indexQueries, User.class);
    }

    @Test
    public void successful() {
        List<Shakespeare> shakespeares = shakespeareService.list(ListRequest.of(0, 1));
        assertThat(shakespeares.size()).isEqualTo(1);
        assertThat(shakespeares.get(0).getLineId()).isEqualTo(4);
        assertThat(shakespeares.get(0).getLineNumber()).isEqualTo("1.1.1");
        assertThat(shakespeares.get(0).getTextEntry()).isEqualTo("So shaken as we are, so wan with care,");

        shakespeares = shakespeareService.list(ListRequest.of(0, 10, "shake"));
        assertThat(shakespeares.get(0).getLineId()).isEqualTo(1571L);
        assertThat(shakespeares.get(0).getLineNumber()).isEqualTo("3.1.21");
        assertThat(shakespeares.get(0).getTextEntry()).isEqualTo("I say the earth did shake when I was born.");

        shakespeares = shakespeareService.list(ListRequest.of(0, 10, "shake").setLastLineId(15773L));
        assertThat(shakespeares.get(0).getLineId()).isEqualTo(15774);
        assertThat(shakespeares.get(0).getLineNumber()).isEqualTo("1.1.28");
        assertThat(shakespeares.get(0).getTextEntry()).isEqualTo("shake me up.");
        assertThat(shakespeares.stream().map(Shakespeare::getLineId).anyMatch(it -> it > 15773L)).isTrue();
    }

    @Test
    public void refresh() throws InterruptedException {
        Query query = new NativeSearchQueryBuilder()
            .withQuery(QueryBuilders.termQuery("id", 1)).build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(query, User.class);
        assertThat(searchHits.getTotalHits()).isEqualTo(0);

        Thread.sleep(1000L);
        searchHits = elasticsearchRestTemplate.search(query, User.class);
        assertThat(searchHits.getTotalHits()).isEqualTo(1);
    }
}
