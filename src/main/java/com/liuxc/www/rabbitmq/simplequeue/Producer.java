package com.liuxc.www.rabbitmq.simplequeue;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author L
 * @date 2018/12/31 - 21:59
 */
public class Producer {

    private static final String SIMPLE_QUEUE = "SIMPLE_QUEUE";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建管道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(SIMPLE_QUEUE, false, false, false, null);
        //绑定队列
        String msg = "hello world!";
        channel.basicPublish("", SIMPLE_QUEUE, null, msg.getBytes());
        System.out.println("send massage" + msg);
        channel.close();
        connection.close();
    }
}
