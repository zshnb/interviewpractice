package com.zshnb.interviewpractice.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {
    public int getNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }
}
