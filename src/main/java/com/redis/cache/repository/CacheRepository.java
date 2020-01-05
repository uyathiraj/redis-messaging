package com.redis.cache.repository;

import com.redis.exception.RedisException;

/**
 * 
 * @author yathiraj
 *
 * @param <T>
 * @param <ID>
 */
public interface CacheRepository<T, ID> {

	public T save(T entity) throws RedisException;

	public T findById(ID id) throws RedisException;

	public T delete(ID id) throws RedisException;

	public T update(T id) throws RedisException;
}
