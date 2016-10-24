package org.xueliang.activemqjms.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 异步接收消息
 * @author XueLiang
 * @date 2016年10月24日 下午5:46:36
 * @version 1.0
 */
public class AsynchronousConsumer {

	public static void main(String[] args) throws JMSException, InterruptedException {
		
		// 创建工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.brokerURL);
		
		// 创建并打开连接
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		// 创建会话	(使用事务，自动发送确认)
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// 创建消息目的地（队列）
		Destination destination = session.createQueue(Constants.defaultQueueName);
		
		// 创建消费者
		MessageConsumer messageConsumer = session.createConsumer(destination);
		messageConsumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					if (message instanceof TextMessage) {
						// 转换消息
						TextMessage textMessage = (TextMessage) message;
						// textMessage.acknowledge(); // 若使用 Session.CLIENT_ACKNOWLEDGE，这里需要手动提交
						
						// 获取内容
						String text = textMessage.getText();
						System.out.println("receive message: " + text);
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
