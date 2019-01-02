package com.liuxc.www.rabbitmq.routing;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.util.Objects;

/**
 * @author L
 * @date 2019/1/1 - 15:15
 */
public class Producer {
    private static final String EXCHANGE_NAME = "exchange_routing_direct";
    private static final String ROUTING_KEY = "error";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //获取通信管道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        //声明交换空间
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //发送消息
        String msg = "routing key!";
        System.out.println("ProducerSingle send msg ==> " + msg );
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, msg.getBytes());
        //关闭连接
        channel.close();
        connection.close();
    }
}
