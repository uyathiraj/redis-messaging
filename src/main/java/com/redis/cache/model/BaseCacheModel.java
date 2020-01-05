package com.redis.cache.model;


import java.io.Serializable;

import com.redis.cache.config.RId;
import com.redis.cache.config.RTable;

@RTable(name = "baseModel")
public class BaseCacheModel implements Serializable{
	
	@RId
	private String id;
	
	private String message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
