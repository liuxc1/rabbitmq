package com.liuxc.www.rabbitmq.argumentsqueue;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author L
 * @date 2019/1/3 - 22:01
 */
public class Consumer {

    private static final String QUEUE_EXPIRATION_NAME = "queue.expiration.test";
    private static final String QUEUE_MAX_PRIORITY_NAME = "queue.max.priority.test";
    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_MAX_PRIORITY_NAME, false, false, false, arguments);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
            }
        };
        channel.basicConsume(QUEUE_MAX_PRIORITY_NAME, true, consumer);
    }
}
