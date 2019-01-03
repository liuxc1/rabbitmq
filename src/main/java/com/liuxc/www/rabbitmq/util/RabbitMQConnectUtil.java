package com.liuxc.www.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author L
 * @date 2018/12/31 - 21:29
 */
public class RabbitMQConnectUtil {

    private static RabbitMQConnectUtil ourInstance = new RabbitMQConnectUtil();

    private RabbitMQConnectUtil() {

    }

    public static RabbitMQConnectUtil getInstance() {
        return ourInstance;
    }

    /**
     * 获取MQ连接
     */
    public static Connection getConnection() {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置ip
        factory.setHost("192.168.2.105");
       // factory.setHost("192.168.20.85");
        //设置AMQP协议端口
        factory.setPort(5672);
        //设置虚拟主机
        factory.setVirtualHost("/my-virtual-hosts");
        //设置用户名
        factory.setUsername("java");
        //设置密码
        factory.setPassword("java");
        try {
            return factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

}
