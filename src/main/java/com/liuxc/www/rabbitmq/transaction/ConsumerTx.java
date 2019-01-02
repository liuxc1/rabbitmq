package com.liuxc.www.rabbitmq.transaction;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author L
 * @date 2019/1/1 - 16:42
 */
public class ConsumerTx {
    private static final String QUEUE_NAME = "transaction_queue";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //获取通信管道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //接受消息
        channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[consumer] 接受到消息 ==> " + new String(body, StandardCharsets.UTF_8));
            }
        });
    }
}
