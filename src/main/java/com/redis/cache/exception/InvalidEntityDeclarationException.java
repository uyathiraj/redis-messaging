package com.redis.cache.exception;

import com.redis.exception.RedisException;

public class InvalidEntityDeclarationException extends RedisException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidEntityDeclarationException(String msg) {
		super(msg);
	}
}
