package com.redis.messaging.client;

import java.time.LocalDateTime;
import java.util.List;

import com.redis.messaging.consumer.RedisMessageConsumer;
import com.redis.messaging.listener.RedisMessageListner;
import com.redis.messaging.model.Message;
import com.redis.messaging.publisher.RedisMessagePublisher;

public class Client {

	public static void main(String[] args) {
		System.out.println("Hello testing redis pub/sub");

		String channelName = "1234567890";

		RedisMessagePublisher<Message> publisher = new RedisMessagePublisher<Message>();
		RedisMessageConsumer<Message> consumer = new RedisMessageConsumer<Message>();
		Message msg = new Message();
		msg.setBody("Hi-3");
		msg.setSender("1234567890");
		msg.setSentTime(LocalDateTime.now());
		System.out.println("Publishing message");
		publisher.publishMessage(msg, "1234567890");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Consumeing message");
		// List<Message> consumedMsgs = consumer.consume("1234567890");

		RedisMessageListner<Message> listener = (consumedMsg) -> {
			System.out.println("Inside the procerssor");

			System.out.println(Thread.currentThread().getName() +">>"+ consumedMsg);
		};
		
		RedisMessageListner<Message> listener2 = (consumedMsg) -> {
			System.out.println("Inside the procerssor");

			System.out.println(Thread.currentThread().getName() +">>"+ consumedMsg);
		};
		
		/// consumer.consume("1234567890", listener);
		consumer.subscribe(channelName, listener2);
		consumer.subscribe(channelName, listener);

	}
}
