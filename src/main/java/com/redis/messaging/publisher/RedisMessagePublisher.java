package com.redis.messaging.publisher;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RFuture;
import org.redisson.api.RQueue;
import org.redisson.api.RQueueAsync;
import org.redisson.api.RedissonClient;

import com.redis.messaging.config.RedisConfig;
import com.redis.messaging.error.RedisMessageException;
import com.redis.messaging.model.Message;

/**
 * 
 * @author yathiraj
 *
 */
public class RedisMessagePublisher<T extends Message> implements MessagePublisher<T> {

	private static final Logger LOGGER = Logger.getLogger("RedisMessagePublisher");

	private RedissonClient client;

	public static <V extends Message> RedisMessagePublisher<V> getNewInstance(Class<V> clazz) {
		return new RedisMessagePublisher<V>();
	}

	public RedisMessagePublisher() {
		client = RedisConfig.getInstance().build();

	}

	@Override
	public void publishMessage(T message) {
		publishMessage(message, message.getChannelName());

	}

	@Override
	public void publishMessage(T message, String channelName) {

		if (!message.isEmpty()) {
			RQueue<Message> queue = client.getQueue(channelName);
			message.setId(generateId());
			RFuture<Boolean> resFuture = queue.addAsync(message);
			resFuture.onComplete((res, e) -> {
				System.out.println("Message has been successfully published Message Id : " + message.getId());
				if (!res) {
					if (e != null) {
						e.printStackTrace();
					}
					retryPublish(message, channelName);
				} else {
					System.out.println("Successfully pushed to ");
				}

			});

		} else {
			LOGGER.warning("Trying to pubish message with empty body");
		}

	}

	@Override
	public void publishBatchMessage(List<T> messages, String channelName) throws RedisMessageException {

		if (messages.isEmpty()) {
			LOGGER.warning("Empty messages are not allowed");
			throw new RedisMessageException("Emtoy message list");
		}

		RBatch batch = client.createBatch();
		RQueueAsync<Message> queue = batch.getQueue(channelName);
		queue.addAllAsync(messages);
		RFuture<BatchResult<?>> futureList = batch.executeAsync();

		futureList.onComplete((res, e) -> {

			System.out.println("Message has been successfully published ");

		});

	}

	@Override
	public void retryPublish(T message, String channelName) {
		int i = 0;
		while (i < RedisConfig.MAX_RETRY) {
			System.out.println("Retrying publish of " + message.getId());
			i++;
			publishMessage(message, channelName);
		}

	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void retryPublish(List<T> messageList, String channelName) {
		// TODO Auto-generated method stub

	}

}
