package com.redis.cache.service;

import java.lang.reflect.Method;

@FunctionalInterface
public interface KeyGenerator {
	
	Object generateKey(Object target,Method method,Object ... objects); 
}
