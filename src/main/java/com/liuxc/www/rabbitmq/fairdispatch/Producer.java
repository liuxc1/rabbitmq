package com.liuxc.www.rabbitmq.fairdispatch;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author L
 * @date 2018/12/31 - 23:23
 */
public class Producer {
    private static final String QUEUE_NAME = "WORK_QUEUE";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建管道
        Channel channel = connection.createChannel();
        /*
         *在每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者端。
         * 限制队列每次只能发送一个消息到消费者
         */
        channel.basicQos(1);
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //发送消息
        for (int i = 0; i < 50; i++) {
            String msg = "work queue " + i;
            System.out.println("ProducerSingle send => " + msg);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        }
        channel.close();
        connection.close();
    }
}
