package com.redis.messaging.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import com.redis.messaging.util.StringUtils;

/**
 * 
 * @author yathiraj
 *
 */

public final class RedisConfig {
	/**
	 * Maximum consumer threads to allocate in thread pool
	 */
	public static int MAX_CONSUMER_THREADS = 5;
	
	/**
	 * Poll timing for consumer for the delay
	 */
	public static int CONSUMER_POLL_TIME = 5;

	private static RedisConfig redisConfig;

	private static String server;

	private static String DEFAULT_HOST = "127.0.0.1";

	private static int DEFAULT_PORT = 6379;

	private static int port;

	private static String host;

	private static boolean isClusterEndPoint = false;

	private RedisConfig() {

	}

	public static RedisConfig getInstance() {
		if (redisConfig == null) {
			redisConfig = new RedisConfig();
		}
		return redisConfig;
	}

	public RedisConfig withHost(String server, int port) {
		RedisConfig.port = port;
		RedisConfig.server = server;
		StringBuilder hostBuilder = new StringBuilder().append("redis://").append(server).append(":").append(port);
		RedisConfig.host = hostBuilder.toString();
		return redisConfig;
	}

	public RedisConfig withClusterEndPoint(boolean flag) {
		RedisConfig.isClusterEndPoint = flag;
		return redisConfig;
	}

	public RedissonClient build() {

		if (StringUtils.isEmpty(RedisConfig.host)) {
			withHost(DEFAULT_HOST, DEFAULT_PORT);
		}

		Config config = new Config();
		if (isClusterEndPoint) {
			config.useClusterServers().addNodeAddress(host);

		} else {
			config.useSingleServer().setAddress(host);

		}
		return Redisson.create(config);
	}

	public static String getServer() {
		return server;
	}

	public static void setServer(String server) {
		RedisConfig.server = server;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		RedisConfig.port = port;
	}

	public static boolean isClusterEndPoint() {
		return isClusterEndPoint;
	}

	public static void setClusterEndPoint(boolean isClusterEndPoint) {
		RedisConfig.isClusterEndPoint = isClusterEndPoint;
	}

}
