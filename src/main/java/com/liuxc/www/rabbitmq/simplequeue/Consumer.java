package com.liuxc.www.rabbitmq.simplequeue;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author L
 * @date 2018/12/31 - 22:23
 */
public class Consumer {
    private static final String SIMPLE_QUEUE = "SIMPLE_QUEUE";

    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建管道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(SIMPLE_QUEUE, false, false, false, null);
        //创建消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            //监听消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumer msg:" + new String(body, "utf-8"));
            }
        };
        //监听队列
        channel.basicConsume(SIMPLE_QUEUE, true,consumer);
    }
}
