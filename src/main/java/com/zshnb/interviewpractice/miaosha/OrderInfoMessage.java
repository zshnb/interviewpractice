package com.zshnb.interviewpractice.miaosha;

import java.time.LocalDateTime;

public class OrderInfoMessage {
    private String userName;
    private LocalDateTime createAt;

    public OrderInfoMessage(String userName, LocalDateTime createAt) {
        this.userName = userName;
        this.createAt = createAt;
    }
}
