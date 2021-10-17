package com.zshnb.interviewpractice.cache_consistency;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

//@Component
public class BinlogStrategy implements CacheConsistencyStrategy {
    private final EntityRepository entityRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    public BinlogStrategy(EntityRepository entityRepository, RedisTemplate<String, Integer> redisTemplate) {
        this.entityRepository = entityRepository;
        this.redisTemplate = redisTemplate;
        new Thread(new BinlogListener(redisTemplate)).start();
    }

    @Override
    public void write() {
        entityRepository.saveAndFlush(new Entity(Thread.currentThread().getName()));
    }

    @Override
    public int read() {
        Integer count = redisTemplate.opsForValue().get(CacheConsistencyStrategy.key);
        return count == null ? (int) entityRepository.count() : count;
    }

    public static class BinlogListener implements Runnable {
        private final RedisTemplate<String, Integer> redisTemplate;

        public BinlogListener(RedisTemplate<String, Integer> redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        @Override
        public void run() {
            // 创建链接
            CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.23.89", 11111), "example", "", "");
            int batchSize = 1000;
            int emptyCount = 0;
            try {
                connector.connect();
                connector.subscribe(".*\\..*");
                connector.rollback();
                int totalEmptyCount = 120;
                while (emptyCount < totalEmptyCount) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        emptyCount++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    } else {
                        emptyCount = 0;
                        handleChangeRow(message.getEntries());
                    }
                    connector.ack(batchId); // 提交确认
                }
            } finally {
                connector.disconnect();
            }
        }

        private void handleChangeRow(List<CanalEntry.Entry> entries) {
            entries.forEach(it -> {
                String tableName = it.getHeader().getTableName();
                redisTemplate.delete(String.format("%s-cache", tableName));
            });
        }
    }
}
