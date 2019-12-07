package com.redis.messaging.publisher;

@FunctionalInterface
public interface RedisMessageListner<T> {

	public void process(T messageList);
}
