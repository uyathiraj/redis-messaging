package com.redis.messaging.publisher;

import java.util.List;

import com.redis.messaging.error.RedisMessageException;
import com.redis.messaging.model.Message;

/**
 * 
 * @author yathiraj
 *
 */
public interface MessagePublisher<T extends Message> {

	void publishMessage(T message);

	void publishMessage(T message, String channelName);

	void publishBatchMessage(List<T> messages, String channelName) throws RedisMessageException;
}
