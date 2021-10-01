package com.zshnb.interviewpractice.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

@Component
public class Rank {
    private final RedisTemplate redisTemplate;
    public static String key = "user-rank";

    public Rank(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addUser(User user) {
        redisTemplate.opsForZSet().add(key, user, user.score);
    }

    public Set<User> topN(int n) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, 0, Integer.MAX_VALUE, 0, n);
    }

    public static class User implements Serializable {
        public User() {
        }

        public User(String name, int score) {
            this.name = name;
            this.score = score;
        }

        String name;
        int score;

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
