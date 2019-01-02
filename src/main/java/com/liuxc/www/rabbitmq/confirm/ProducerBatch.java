package com.liuxc.www.rabbitmq.confirm;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author L
 * @date 2018/12/31 - 23:23
 */
public class ProducerBatch {
    private static final String QUEUE_NAME = "queue_confirm";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建管道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //开始confirm 模式
        channel.confirmSelect();
        //发送消息
        for (int i = 0; i < 10; i++) {
            String msg = "confirm !" + i;
            System.out.println("producer single send msg " + msg);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        }
        //消息确认
        if (channel.waitForConfirms()) {
            System.out.println("消息发送成功！");
        } else {
            System.out.println("消息发送失败！");
        }
        //关闭连接
        channel.close();
        connection.close();
    }
}
