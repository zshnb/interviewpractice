package com.zshnb.interviewpractice.elasticsearch;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.zshnb.interviewpractice.BaseTest;
import com.zshnb.interviewpractice.elasticsearch.ShakespeareService.ListRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShakespeareServiceTest extends BaseTest {
    @Autowired
    private ShakespeareService shakespeareService;

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
}
