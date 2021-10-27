package com.zshnb.interviewpractice.miaosha;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//@Component
public class PrepareDataApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final RedisTemplate<String, Integer> redisTemplate;
    private final ProductRepository productRepository;

    public PrepareDataApplicationListener(RedisTemplate<String, Integer> redisTemplate, ProductRepository productRepository) {
        this.redisTemplate = redisTemplate;
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        redisTemplate.opsForValue().set("testProduct", 10);
        Product product = new Product();
        product.setName("testProduct");
        product.setStock(10);
        productRepository.save(product);
    }
}
