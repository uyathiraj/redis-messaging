package com.redis.messaging.client;

import java.time.LocalDateTime;

import com.redis.messaging.consumer.RedisMessageConsumer;
import com.redis.messaging.listener.RedisMessageListner;
import com.redis.messaging.model.Message;
import com.redis.messaging.publisher.RedisMessagePublisher;

public class Client {

	public static void main(String[] args) {
		System.out.println("Hello testing redis pub/sub");

		String channelName = "1234567890";

		RedisMessagePublisher<Message> publisher = RedisMessagePublisher.getNewInstance(Message.class);
		RedisMessageConsumer<Message> consumer = RedisMessageConsumer.getNewInstance(Message.class);


		System.out.println("Consumeing message");
		// List<Message> consumedMsgs = consumer.consume("1234567890");

		RedisMessageListner<Message> listener = (consumedMsg) -> {
			System.out.println("Inside the procerssor");

			System.out.println(Thread.currentThread().getName() + ">>" + consumedMsg);
		};

		

		/// consumer.consume("1234567890", listener);
		//consumer.subscribe(channelName, listener2);
		consumer.subscribe(channelName, listener);
		
		
		for(int i=0;i<100000;i++) {
			Message msg = new Message();
			msg.setBody("Hi-"+i);
			msg.setSender("1234567890");
			msg.setSentTime(LocalDateTime.now());
			System.out.println("Publishing message");
			publisher.publishMessage(msg, "1234567890");
		}
		

	}
}
