package it.tests.springcamel;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringcamelApplicationTests {


	public static final String TEST_TOPIC = "test";
	@ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, TEST_TOPIC);

	@Test
	public void contextLoads() {
	}

	private KafkaMessageListenerContainer<String, String> container;

	private BlockingQueue<ConsumerRecord<String, String>> records;


	@Test
	public void shouldReceiveAtLeastTwoMessagesIn20Seconds() throws InterruptedException {
		System.out.println("Waiting 20 seconds");
		Thread.sleep(20000);

		Assert.assertTrue(records.size() > 2);

	}

	@Before
	public void setUp() throws Exception {
		// set up the Kafka consumer properties
		Map<String, Object> consumerProperties =
				KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);

		// create a Kafka consumer factory
		DefaultKafkaConsumerFactory<String, String> consumerFactory =
				new DefaultKafkaConsumerFactory<String, String>(consumerProperties);

		// set the topic that needs to be consumed
		ContainerProperties containerProperties = new ContainerProperties(TEST_TOPIC);

		// create a Kafka MessageListenerContainer
		container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

		// create a thread safe queue to store the received message
		records = new LinkedBlockingQueue<>();

		// setup a Kafka message listener
		container.setupMessageListener((MessageListener<String, String>) e -> {
			System.out.println("RECEIVING MESSAGE FROM KAFKA!!!!");
			records.add(e);

		});

		// start the container and underlying message listener
		container.start();

		// wait until the container has the required number of assigned partitions
		ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
	}

	@After
	public void tearDown() {
		// stop the container
		container.stop();
	}

}
