package com.liuxc.www.rabbitmq.workqueue;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author L
 * @date 2018/12/31 - 23:30
 */
public class Consumer2 {

    private static final String QUEUE_NAME = "WORK_QUEUE";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //创建消费者，监听消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("[Consumer2]接受到消息==> " + new String(body, "utf-8"));
                //延时一秒，模拟不同服务器的响应时间
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        //加入监听
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
