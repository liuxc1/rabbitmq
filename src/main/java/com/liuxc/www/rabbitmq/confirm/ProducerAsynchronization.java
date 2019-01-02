package com.liuxc.www.rabbitmq.confirm;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author L
 * @date 2018/12/31 - 23:23
 */
public class ProducerAsynchronization {
    private static final String QUEUE_NAME = "queue_confirm";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建管道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //存储未确认的信息主键
        SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        //开始confirm 模式
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("多条一起确认");
                    confirmSet.headSet(deliveryTag + 1L).clear();
                } else {
                    System.out.println("一条一起确认");
                    confirmSet.remove(deliveryTag);
                }
            }

            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1L).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }
        });
        //发送消息
        for (int i = 0; i < 10; i++) {
            long nextSeqNo = channel.getNextPublishSeqNo();
            String msg = "confirm !" + i;
            System.out.println("producer single send msg " + msg);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            confirmSet.add(nextSeqNo);
        }
        //关闭连接
        channel.close();
        connection.close();
    }
}
