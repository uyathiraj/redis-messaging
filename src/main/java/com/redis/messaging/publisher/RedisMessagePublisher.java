package com.redis.messaging.publisher;

import java.util.List;
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
	public void publishMessage(Message message) {
		publishMessage(message, message.getChannelName());

	}

	@Override
	public void publishMessage(Message message, String channelName) {
		
		if (!message.isEmpty()) {
			RQueue<Message> queue = client.getQueue(channelName);
			queue.addAsync(message);

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

		futureList.onComplete((b, e) -> {
			System.out.println("Completed publish");
			System.err.println(e.getMessage());
		});

	}

}
