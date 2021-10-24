## 秒杀系统设计

### 描述

使用spring boot+jpa框架，使用了redis和kafka中间件

### 运行说明

1. 运行redis 
2. 运行kafka
3. 运行mysql
4. 启动项目
5. 在jmeter里创建测试计划
6. 运行jmeter测试计划

### 测试结果（50个请求/1s）

![](C:\Users\zsh\Workbench\zshnb\interviewpractice\src\main\java\com\zshnb\interviewpractice\miaosha\result.png)

### 总结

在项目启动时初始化商品信息在redis和数据库中，请求进来时先通过redis的decr命令预扣库存，预扣成功后的请求进入数据库进行实际扣除，只有数据库扣除成功的请求才属于抢购成功，抢购成功后将订单信息发送到kafka中，消费异步消费创建订单，流程结束。
