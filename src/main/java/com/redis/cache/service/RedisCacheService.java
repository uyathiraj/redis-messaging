package com.redis.cache.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import com.redis.messaging.config.RedisConfig;
/**
 * 
 * @author yathiraj
 *
 */
public class RedisCacheService implements CacheService {

	private RedissonClient redisClient;

	private static final String CACHE_NAME = "test";

	public RedisCacheService() {
		redisClient = RedisConfig.getInstance().build();

	}
	
	public static RedisCacheService getInstance() {
		return new RedisCacheService();
	}
	
	@Override
	public void store(String key, Object data, long ttl, TimeUnit unit) {
		RMapCache<String, Object> cahce = redisClient.getMapCache(CACHE_NAME);
		cahce.put(key, data, ttl, unit);

	}

	@Override
	public Object get(String key) {
		RMapCache<String, Object> cachce = redisClient.getMapCache(CACHE_NAME);
		return cachce.get(key);
	}

	@Override
	public void store(String key, Object data) {
		RMapCache<String, Object> cahce = redisClient.getMapCache(CACHE_NAME);
		cahce.put(key, data);
	}
}
