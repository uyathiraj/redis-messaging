package com.redis.messaging.listener;

@FunctionalInterface
public interface RedisMessageListner<T> {

	public void process(T messageList);
}
