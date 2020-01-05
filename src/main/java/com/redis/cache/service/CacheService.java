package com.redis.cache.service;

import java.util.concurrent.TimeUnit;

public interface CacheService {

	public void store(String key, Object data);

	public Object get(String key);

	void store(String key, Object data, long ttl, TimeUnit unit);
	
}
