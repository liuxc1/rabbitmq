package com.liuxc.www.rabbitmq.workqueue;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

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
