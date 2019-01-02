package com.liuxc.www.rabbitmq.fairdispatch;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author L
 * @date 2018/12/31 - 23:30
 */
public class Consumer1 {

    private static final String QUEUE_NAME = "WORK_QUEUE";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建通道
        final Channel channel = connection.createChannel();
        //1次只处理一个消息。处理完成后在接受新的消息
        channel.basicQos(1);
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //创建消费者，监听消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[Consumer1]接受到消息==> " + new String(body, "utf-8"));
                //延时一秒，模拟不同服务器的响应时间
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //手动应道
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAck = false;//关闭自动应答，使用手动应答.确保每条消息成功执行
        //加入监听
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
