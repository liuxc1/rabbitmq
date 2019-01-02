package com.liuxc.www.rabbitmq.publishsubscribe;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author L
 * @date 2018/12/31 - 23:23
 */
public class Producer {
    private static final String EXCHANGE_NAME = "EXCHANGE_PUBLISH_SUBSCRIBE_FANOUT";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建管道
        Channel channel = connection.createChannel();
        //声明交换空间
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //发送消息
        String msg = "publish/subscribe";
        System.out.println("ProducerSingle send => " + msg);
        channel.basicPublish(EXCHANGE_NAME,"", null, msg.getBytes());
        channel.close();
        connection.close();
    }
}
