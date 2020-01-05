package com.redis.messaging.consumer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import com.redis.messaging.config.RedisConfig;
import com.redis.messaging.error.RedisMessageException;
import com.redis.messaging.listener.RedisMessageListner;
import com.redis.messaging.model.Message;
import com.redis.messaging.publisher.RedisMessagePublisher;

/**
 * 
 * @author yathiraj
 *
 * @param <T>
 */

public class RedisMessageConsumer<T extends Message> implements MessageConsumer<T> {

	private static final Logger LOGGER = Logger.getLogger("RedisMessageConsumer");

	private static ExecutorService executor;

	private static RedisMessagePublisher<Message> deadQueueLetterPublisher;

	static {
		RedisMessageConsumer.executor = Executors.newFixedThreadPool(RedisConfig.MAX_CONSUMER_THREADS);
	}

	private RedissonClient client;

	private Map<String, Set<RedisMessageListner<T>>> listenersMap = new ConcurrentHashMap<>();

	public static <V extends Message> RedisMessageConsumer<V> getNewInstance(Class<V> clazz) {

		if (deadQueueLetterPublisher != null) {
			deadQueueLetterPublisher = RedisMessagePublisher.getNewInstance(Message.class);

		}
		return new RedisMessageConsumer<V>();
	}

	public RedisMessageConsumer() {
		this.client = RedisConfig.getInstance().build();
	}

	public T consume(String channelName) throws InterruptedException {
		RBlockingQueue<T> queue = client.getBlockingQueue(channelName);

		T msg = queue.poll(RedisConfig.CONSUMER_POLL_TIME, TimeUnit.SECONDS);
		return msg;
	}

	@Override
	public void consume(String channelName, RedisMessageListner<T> listner) throws RedisMessageException {
		RBlockingQueue<T> queue = client.getBlockingQueue(channelName);
		System.out.println("Consuming messages from " + channelName);
		executor.execute(() -> {
			while (true) {
				T msg = null;
				try {

					msg = queue.poll(RedisConfig.CONSUMER_POLL_TIME, TimeUnit.SECONDS);
					if (msg != null) {
						listner.process(msg);
					}

				} catch (InterruptedException e) {

					if (RedisConfig.enableDeadLetterQueue) {
						RedisMessageConsumer.deadQueueLetterPublisher.publishMessage(msg,
								RedisConfig.DEAD_LETTER_QUEUE);

					}

				}

			}
		});

	}

	@Override
	public void subscribe(String channelName, RedisMessageListner<T> listener) {

		Set<RedisMessageListner<T>> listenerSet = listenersMap.getOrDefault(channelName,
				new HashSet<RedisMessageListner<T>>());
		listenerSet.add(listener);
		executor.execute(() -> {
			try {
				consume(channelName, listener);
			} catch (RedisMessageException e) {
				e.printStackTrace();
			}
		});
		listenersMap.put(channelName, listenerSet);
		LOGGER.info("Listener subscribed to " + channelName);
	}

	@Override
	public void subscribe(String channelName, Set<RedisMessageListner<T>> listenrList) {
		listenrList.forEach(item -> subscribe(channelName, item));

	}

}
