package com.redis.client;

import java.util.concurrent.TimeUnit;

import com.redis.cache.model.BaseCacheModel;
import com.redis.cache.repository.RedisCacheRepository;
import com.redis.cache.service.RedisCacheService;

public class Client {

	public static void main(String[] args) throws InterruptedException {
		//testCache();
		testStore();
	}

	private static String message() {
		System.out.println("Not from cache :(");
		return "Hello world";
	}
	
	private static void testCache() throws InterruptedException {
		RedisCacheService service = RedisCacheService.getInstance();
		service.store("hello", message(), 15, TimeUnit.SECONDS);
		for (int i = 0; i < 5; i++) {
			System.out.println(service.get("hello"));
			Thread.sleep(5000);
		}
	}
	
	private static void testStore() {
		BaseCacheModel model = new BaseCacheModel();
		model.setId("1234567");
		model.setMessage("Hello world");
		RedisCacheRepository<BaseCacheModel, String> repo = new RedisCacheRepository<>();
		repo.save(model);
		System.out.println("Getting data");
		BaseCacheModel modelDb = repo.findById("1234567");
		System.out.println(modelDb.getMessage());
	}
}
