package com.redis.messaging.error;

/**
 * 
 * @author yathiraj
 *
 */
public class RedisMessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedisMessageException(String msg) {
		super(msg);
	}
	
	
	public RedisMessageException(Throwable e) {
		super(e);
	}

}
