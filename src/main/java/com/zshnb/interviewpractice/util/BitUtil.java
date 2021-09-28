package com.zshnb.interviewpractice.util;

import org.springframework.stereotype.Component;

@Component
public class BitUtil {
    public String byteToBit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) +
            (byte) ((b >> 6) & 0x1) +
            (byte) ((b >> 5) & 0x1) +
            (byte) ((b >> 4) & 0x1) +
            (byte) ((b >> 3) & 0x1) +
            (byte) ((b >> 2) & 0x1) +
            (byte) ((b >> 1) & 0x1) +
            (byte) ((b) & 0x1);
    }
}
