package com.liuxc.www.rabbitmq.transaction;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Objects;

/**
 * @author L
 * @date 2019/1/1 - 16:36
 */
public class ProducerTx {
    private static final String QUEUE_NAME = "transaction_queue";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //获取通信管道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        try {
            //开启事物
            channel.txSelect();
            //int i = 1/0;
            //发送消息
            String msg = "transation  !";
            System.out.println("ProducerSingle send msg ==> " + msg);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            //提交事物
            channel.txCommit();
        } catch (Exception e) {
            e.printStackTrace();
            channel.txRollback();
        }
        //关闭连接
        channel.close();
        connection.close();
    }
}
