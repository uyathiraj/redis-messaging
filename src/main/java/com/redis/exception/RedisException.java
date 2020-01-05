package com.redis.exception;

public class RedisException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedisException() {
		super();
	}

	public RedisException(Throwable e) {
		super(e);
	}

	public RedisException(String msg) {
		super(msg);
	}
}
