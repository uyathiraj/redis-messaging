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

public class RedisMessageConsumer<T extends Message> implements MessageConsumer<T> {

	private static final Logger LOGGER = Logger.getLogger("RedisMessageConsumer");

	private ExecutorService executor = Executors.newFixedThreadPool(RedisConfig.MAX_CONSUMER_THREADS);

	private RedissonClient client;

	private Map<String, Set<RedisMessageListner<T>>> listenersMap = new ConcurrentHashMap<>();

	public static <V extends Message> RedisMessageConsumer<V> getNewInstance(Class<V> clazz) {
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
		while (true) {
			
			try {
				T msg = queue.poll(RedisConfig.CONSUMER_POLL_TIME, TimeUnit.SECONDS);
				if (msg != null) {
					listner.process(msg);
				}
			} catch (InterruptedException e) {
				throw new RedisMessageException(e);
			}
			
		}

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
