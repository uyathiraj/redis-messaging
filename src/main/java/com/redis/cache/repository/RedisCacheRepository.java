package com.redis.cache.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import com.redis.cache.config.RId;
import com.redis.cache.config.RTable;
import com.redis.cache.exception.InvalidEntityDeclarationException;
import com.redis.messaging.config.RedisConfig;

public class RedisCacheRepository<T, ID> implements CacheRepository<T, ID> {

	private RedissonClient client;
	
	
	private String BUCKET_NAME = "mybucket";

	public RedisCacheRepository() {
		client = RedisConfig.getInstance().build();
	}

	@Override
	public T save(T entity) {

		Field[] fileds = entity.getClass().getDeclaredFields();
		RTable[] tableAnn = entity.getClass().getAnnotationsByType(RTable.class);
		if (tableAnn == null || tableAnn.length == 0) {
			throw new InvalidEntityDeclarationException("Entity is not decalred properly");
		}
		//String bucketName = tableAnn[0].name();
		String bucketName = BUCKET_NAME;
		Field idField = null;
		for (Field f : fileds) {

			Annotation[] anns = f.getDeclaredAnnotationsByType(RId.class);
			if (anns != null) {
				idField = f;
				break;
			}

		}
		if (idField == null) {
			throw new InvalidEntityDeclarationException("Index field doesn't exists.");
		}
		idField.setAccessible(true);
		try {
			ID key = (ID) idField.get(entity);
			RMap<ID, T> map = client.getMap(bucketName);
			map.put(key, entity);

		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public T findById(ID id) {
		try {
			/// System.out.println(bucketName);
			// String bucketName = t.getClass().getSimpleName();
			//ParameterizedType paramType = (ParameterizedType) this.getClass().getGenericInterfaces()[0];
			//Class<T> clazz = (Class<T>) paramType.getActualTypeArguments()[0].getClass();
			
			//System.out.println(clazz.newInstance().toString());
			String bucketName = BUCKET_NAME;
			RMap<ID, T> map = client.getMap(bucketName);
			return map.get(id);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public T delete(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T update(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

}
