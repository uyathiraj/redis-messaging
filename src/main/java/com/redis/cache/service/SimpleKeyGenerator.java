package com.redis.cache.service;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SimpleKeyGenerator implements KeyGenerator {

	@Override
	public Object generateKey(Object target, Method method, Object... objects) {
		return Arrays.deepHashCode(objects);
	}

}
