package com.redis.messaging.consumer;

import java.util.Set;

import com.redis.messaging.listener.RedisMessageListner;
import com.redis.messaging.model.Message;

import reactor.util.annotation.NonNull;

/**
 * 
 * @author yathiraj
 *
 */
public interface MessageConsumer<T extends Message> {

	T consume(@NonNull String channelName) throws InterruptedException;

	void consume(@NonNull String channelName, RedisMessageListner<T> listner);

	void subscribe(@NonNull String channelName, RedisMessageListner<T> listener);

	void subscribe(@NonNull String channelName, Set<RedisMessageListner<T>> listenrList);

}
