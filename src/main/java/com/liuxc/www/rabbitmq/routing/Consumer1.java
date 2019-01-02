package com.liuxc.www.rabbitmq.routing;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @author L
 * @date 2019/1/1 - 15:25
 */
public class Consumer1 {
    private static final String QUEUE_NAME = "queue_routing_direct_error";
    private static final String EXCHANGE_NAME = "exchange_routing_direct";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //获取通信管道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        channel.basicQos(1);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定交换空间
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"error");
        //接受消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[consumer 1 ]接受到消息 ===> " + new String(body, "UTF-8"));
                //手动确认消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(QUEUE_NAME,consumer);
    }
}
