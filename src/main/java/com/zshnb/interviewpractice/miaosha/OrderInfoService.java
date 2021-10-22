package com.zshnb.interviewpractice.miaosha;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderInfoService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private final ProductRepository productRepository;

    public OrderInfoService(RedisTemplate<String, Integer> redisTemplate, ProductRepository productRepository) {
        this.redisTemplate = redisTemplate;
        this.productRepository = productRepository;
    }

    @Transactional
    public void createOrderInfo(String productName, String userName) {
        Product product = productRepository.findByName(productName);
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.decrement(productName) >= 0) {
            if (productRepository.updateStock(product.getId()) > 0) {
                System.out.println("抢购成功");
            }
        }
        System.out.println("抢购失败");
    }
}
