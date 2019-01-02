package com.liuxc.www.rabbitmq.publishsubscribe;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author L
 * @date 2019/1/1 - 1:25
 */
public class ConsumerNote {

    private static final String QUEUR_NAME = "USER_NOTE";
    private static final String EXCHANGE_NAME = "EXCHANGE_PUBLISH_SUBSCRIBE_FANOUT";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建通信管道
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        //声明队列
        channel.queueDeclare(QUEUR_NAME, false, false, false, null);
        //绑定到交换空间
        channel.queueBind(QUEUR_NAME, EXCHANGE_NAME, "");
        //监听消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[Note Cosumer] ===> " + new String(body, "UTF-8"));
                //手动应答
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(QUEUR_NAME,false,consumer);
    }
}
