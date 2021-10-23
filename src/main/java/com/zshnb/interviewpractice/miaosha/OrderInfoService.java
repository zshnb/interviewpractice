package com.zshnb.interviewpractice.miaosha;

import java.time.LocalDateTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderInfoService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private final ProductRepository productRepository;
    private final OrderInfoRepository orderInfoRepository;

    public OrderInfoService(RedisTemplate<String, Integer> redisTemplate,
                            ProductRepository productRepository,
                            OrderInfoRepository orderInfoRepository) {
        this.redisTemplate = redisTemplate;
        this.productRepository = productRepository;
        this.orderInfoRepository = orderInfoRepository;
    }

    @Transactional
    public void createOrderInfo(String productName) {
        Product product = productRepository.findByName(productName);
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.decrement(productName) >= 0) {
            if (productRepository.updateStock(product.getId()) > 0) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setUserName(Thread.currentThread().getName());
                orderInfo.setCreateAt(LocalDateTime.now());
                orderInfoRepository.save(orderInfo);
                System.out.println("抢购成功");
            }
        }
        System.out.println("抢购失败");
    }
}
