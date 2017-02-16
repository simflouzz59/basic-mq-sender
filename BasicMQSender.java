import java.util.UUID;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.ibm.mq.jms.MQConnectionFactory;

public class BasicMQSender {

	/**
	 * @param args
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws JMSException {
		String channel = "MY.MQ.CHANNEL";
		String queueManager = "QUEUEMANAGER";
		String queueName = "MY.QUEUE.NAME";
		String hostName = "my.hostname";
		int port = 1414;
		// 1 = TCP
		int transportType = 1;
		
		UUID randomUUID = UUID.randomUUID();
		
		MQConnectionFactory factory = new MQConnectionFactory();
		factory.setHostName(hostName);
		factory.setTransportType(transportType);
		factory.setChannel(channel);
		factory.setQueueManager(queueManager);
		factory.setPort(port);
		
		Connection con = factory.createConnection();
		
		Session session = null;
		
		try {
			session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);

			Queue queue = session.createQueue(queueName);

			MessageProducer producer = session.createProducer(queue);
			Message msg = session.createTextMessage("Hello queue, my uid is : " + randomUUID);
			producer.send(msg);
			session.commit();
		} catch (JMSException e) {
			session.rollback();
			e.printStackTrace();
		}
		finally {
			con.close();
		}
	}
	
}
