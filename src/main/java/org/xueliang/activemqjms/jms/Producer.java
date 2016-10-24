package org.xueliang.activemqjms.jms;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息生产者
 * @author XueLiang
 * @date 2016年10月24日 上午10:42:25
 * @version 1.0
 */
public class Producer {

	public static void main(String[] args) throws JMSException, InterruptedException {
		String textMessageTemplateString = "Hello Receiver! Now is %s";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// 创建工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.brokerURL);
		
		// 创建并打开连接
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		// 创建会话	(不使用事务，自动发送确认)
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// 创建消息目的地（队列）
		Destination destination = session.createQueue(Constants.defaultQueueName);
		
		// 创建生产者
		MessageProducer messageProducer = session.createProducer(destination);
		
		for (int i = 0; i < 1000; i++) {
			String textMessageString = String.format(textMessageTemplateString, simpleDateFormat.format(new Date()));
			
			// 创建文本消息
			TextMessage textMessage = session.createTextMessage(textMessageString);
			
			// 发送消息
			try {
				messageProducer.send(textMessage);
				// session.commit(); // 若使用事务，这里需要提交
				System.out.println("send a message: " + textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
				// session.rollback(); // 若使用事务，这里需要回滚
			} finally {
				Thread.sleep(100);
			}
		}
	}
}
