package cn.hk.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class ActiveMqTest {
    /**
     * 点到点形式发送消息
     * @throws Exception
     */
    @Test
    public void testQueueProducer() throws Exception {
        // 创建连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "tcp://172.16.81.129:61616");
        // 使用工厂对象创建Connection对象
        Connection connection = connectionFactory.createConnection();
        // 开启连接，调用Connection对象start方法
        connection.start();
        // 创建一个session对象
        //第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
        //第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 使用session创建一个Destination对象 两种形式queue topic
        Queue queue = session.createQueue("test-queue");
        // 创建一个producer
        MessageProducer producer = session.createProducer(queue);
        // 创建一个MessageText对象
        TextMessage textMessage = session.createTextMessage("hello activeMq, 我还在给你发消息");
        // 发送消息
        producer.send(textMessage);
        // 关闭资源
        producer.close();
        session.close();
        connection.close();

    }
    @Test
    public void testQueueConsumer() throws Exception {
        // 创建连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "tcp://172.16.81.129:61616");
        // 使用工厂对象创建Connection对象
        Connection connection = connectionFactory.createConnection();
        // 开启连接，调用Connection对象start方法
        connection.start();
        // 创建一个session对象
        //第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
        //第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 使用session创建一个Destination对象 两种形式queue topic
        Queue queue = session.createQueue("test-queue");
        // 创建一个producer
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    String text = textMessage.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();
        // 关闭资源
        consumer.close();
        session.close();
        connection.close();

    }


    @Test
    public void testTopicProducer() throws Exception {
        // 创建连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "tcp://172.16.81.129:61616");
        // 使用工厂对象创建Connection对象
        Connection connection = connectionFactory.createConnection();
        // 开启连接，调用Connection对象start方法
        connection.start();
        // 创建一个session对象
        //第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
        //第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 使用session创建一个Destination对象 两种形式queue topic
        Topic topic = session.createTopic("test-topic");
        // 创建一个producer
        MessageProducer producer = session.createProducer(topic);
        // 创建一个MessageText对象
        TextMessage textMessage = session.createTextMessage("消费者们你们好");
        // 发送消息
        producer.send(textMessage);
        // 关闭资源
        producer.close();
        session.close();
        connection.close();

    }
    @Test
    public void testTopicConsumer() throws Exception {
        // 创建连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "tcp://172.16.81.129:61616");
        // 使用工厂对象创建Connection对象
        Connection connection = connectionFactory.createConnection();
        // 开启连接，调用Connection对象start方法
        connection.start();
        // 创建一个session对象
        //第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
        //第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 使用session创建一个Destination对象 两种形式queue topic
        Topic topic = session.createTopic("test-topic");
        // 创建一个producer
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("消费者3：");

        System.in.read();
        // 关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}
