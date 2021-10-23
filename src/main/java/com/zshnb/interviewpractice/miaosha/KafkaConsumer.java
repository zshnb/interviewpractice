package com.zshnb.interviewpractice.miaosha;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private final OrderInfoRepository orderInfoRepository;

    public KafkaConsumer(
        OrderInfoRepository orderInfoRepository) {
        this.orderInfoRepository = orderInfoRepository;
    }

    @KafkaListener(topics = {"testProduct"})
    public void consume(ConsumerRecord<String, String> record) {
        String value = record.value();
        OrderInfo orderInfo = new Gson().fromJson(value, OrderInfo.class);
        orderInfoRepository.save(orderInfo);
    }
}
