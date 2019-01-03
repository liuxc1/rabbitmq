package com.liuxc.www.rabbitmq.argumentsqueue;

import com.liuxc.www.rabbitmq.util.RabbitMQConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author L
 * @date 2019/1/3 - 20:59
 */
public class Producer {
    private static final String QUEUE_DURABLE_NAME = "queue.durable.test";
    private static final String QUEUE_MESSAGE_TTL_NAME = "queue.message.ttl.test";
    private static final String QUEUE_EXPIRATION_NAME = "queue.expiration.test";
    private static final String QUEUE_AUTO_EXPIRE_NAME = "queue.auto.expire.test";
    private static final String QUEUE_MAX_LENGTH_NAME = "queue.max.length.test";
    private static final String QUEUE_MAX_PRIORITY_NAME = "queue.max.priority.test";

    public static void main(String[] args) throws IOException, TimeoutException {
        // durableQueue();
        // messageTTL();
        // expiration();
        // autoExpire();
        // maxLength();
        // maximumPriority();
        dead();
    }

    /**
     * 声明一个持久化的队列 并且消息持久化
     */
    private static void durableQueue() throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        //声明一个持久化的队列
        channel.queueDeclare(QUEUE_DURABLE_NAME, true, false, false, null);
        //发送消息
        String msg = "创建持久化的队列+消息持久化";
        // 设置消息持久化 方式一
        /*
            设置消息是否持久化，1： 非持久化 2：持久化
            AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder();
            properties.deliveryMode(2);
            channel.basicPublish("", QUEUE_DURABLE_NAME, properties.build(), msg.getBytes());
        */
        // 设置消息持久化 方式二
        channel.basicPublish("", QUEUE_DURABLE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
        //关闭连接
        channel.close();
        connection.close();
    }

    /**
     * Message TTL消息剩余生存时间
     */
    private static void messageTTL() throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();

        // 统一设置队列中的所有消息的过期时间 10秒后这个队列的消息清零
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-message-ttl", 1000 * 10);
        channel.queueDeclare(QUEUE_MESSAGE_TTL_NAME, false, false, false, arguments);
        for (int i = 0; i < 5; i++) {
            String msg = "消息" + i;
            channel.basicPublish("", QUEUE_MESSAGE_TTL_NAME, null, msg.getBytes());
        }
        //关闭连接
        channel.close();
        connection.close();
    }

    /**
     * 单独为某条消息单独设置时间
     */
    private static void expiration() throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        channel.queueDeclare(QUEUE_EXPIRATION_NAME, false, false, false, null);
        for (int i = 1; i <= 5; i++) {
            String msg = "消息存活时间 " + i * 5000;
            System.out.println(msg);
            AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder().expiration(i * 5000 + "");
            channel.basicPublish("", QUEUE_EXPIRATION_NAME, properties.build(), msg.getBytes());

        }
        //关闭连接
        channel.close();
        connection.close();
    }

    /**
     * Auto Expire自动过期
     * x-expires用于当多长时间没有消费者访问该队列的时候，该队列会自动删除，
     * 可以设置一个延迟时间，如仅启动一个生产者，10秒之后该队列会删除，或者启动一个生产者，再启动一个消费者，消费者运行结束后10秒，队列也会被删除
     */

    private static void autoExpire() throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-expires", 10000);
        channel.queueDeclare(QUEUE_AUTO_EXPIRE_NAME, false, false, false, arguments);

        //关闭连接
        channel.close();
        connection.close();
    }

    /**
     * Max Length最大长度
     * x-max-length:用于指定队列的长度，如果不指定，可以认为是无限长，例如指定队列的长度是4，当超过4条消息，前面的消息将被删除，给后面的消息腾位
     */
    private static void maxLength() throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-max-length", 4);
        channel.queueDeclare(QUEUE_MAX_LENGTH_NAME, false, false, false, arguments);
        for (int i = 0; i < 5; i++) {
            String msg = "消息 " + i + "!";
            channel.basicPublish("", QUEUE_MAX_LENGTH_NAME, null, msg.getBytes());
        }
        //关闭连接
        channel.close();
        connection.close();
    }
    /**
     * Maximum priority最大优先级
     * x-max-priority: 设置消息的优先级，优先级值越大，越被提前消费。
     * 优先级队列，声明队列时先定义最大优先级值(定义最大值一般不要太大)，在发布消息的时候指定该消息的优先级， 优先级更高（数值更大的）的消息先被消费,
     */
    private static void maximumPriority() throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_MAX_PRIORITY_NAME, false, false, false, arguments);
        for (int i = 0; i < 5; i++) {
            String msg = "消息 " + i + "!";
            System.out.println(msg);
            AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder().priority(i);
            channel.basicPublish("", QUEUE_MAX_PRIORITY_NAME, properties.build(), msg.getBytes());
        }
        //关闭连接
        channel.close();
        connection.close();
    }

    /**
     * Dead letter exchange(死亡交换机)
     * Dead letter routing key(死亡路由键)
     * 当队列中的消息过期，或者达到最大长度而被删除，或者达到最大空间时而被删除时，可以将这些被删除的信息推送到其他交换机中，
     * 让其他消费者订阅这些被删除的消息，处理这些消息
     */
    private static final String DEAD_EXCHANGE_NAME = "dead.EXCHANGE.test";
    private static final String DEAD_QUEUE_NAME = "dead.queue.test";
    private static final String DEAD_ROUT_KRY = "dead.key";
    private static final String QUEUE_OUTMODED = "queue.outmoded";

    private static void dead() throws IOException, TimeoutException {
        //获取连接
        Connection connection = RabbitMQConnectUtil.getConnection();
        //创建信道
        Channel channel = Objects.requireNonNull(connection).createChannel();
       //声明死信交换空间
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE_NAME,false,false,false,null);
        //队列绑定到交换空间
        channel.queueBind(DEAD_QUEUE_NAME,DEAD_EXCHANGE_NAME,DEAD_ROUT_KRY);

        //创建正常的队列
        Map<String, Object> arguments = new HashMap<String, Object>();
        //队列中消息过期时间
        arguments.put("x-message-ttl", 15000);
        //队列最大长度
        arguments.put("x-max-length", 4);
        //队列过期时间 自动删除队列
        arguments.put("x-expires", 30000);
        //死信交换空间
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", DEAD_ROUT_KRY);
        channel.queueDeclare(QUEUE_OUTMODED,false,false,false,arguments);

        for (int i = 0; i < 5; i++) {
            String msg = "消息 " + i + "!";
            System.out.println(msg);
            channel.basicPublish("", QUEUE_OUTMODED,null, msg.getBytes());
        }

        //关闭连接
        channel.close();
        connection.close();
    }
}
