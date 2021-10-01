package com.zshnb.interviewpractice.redis;

import com.zshnb.interviewpractice.BaseTest;
import com.zshnb.interviewpractice.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RankTest extends BaseTest {

    @Autowired
    private Rank rank;

    @Autowired
    private RandomUtil randomUtil;

    private List<Rank.User> users;

    @Override
    public void beforeSetup() {
        users = new ArrayList<>(120);
        IntStream.range(0, 100).forEach(it -> {
            users.add(new Rank.User(String.format("user%d", it), randomUtil.getNumber(0, 10000)));
            rank.addUser(users.get(it));
        });
    }

    @Test
    public void top10() {
        Rank.User firstUser = users.stream()
            .max(Comparator.comparingInt(Rank.User::getScore))
            .get();

        Set<Rank.User> users = rank.topN(10);
        assertThat(users.size()).isEqualTo(10);
        assertThat(users.stream().findFirst().get().name).isEqualTo(firstUser.name);
        assertThat(users.stream().findFirst().get().score).isEqualTo(firstUser.score);
    }
}
