package com.jt.common.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisSentinelPool;
//import redis.clients.jedis.ShardedJedis;
//import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
	
	/**
	 * 哨兵redis+连接池
	 */
	
	@Autowired(required=false)
	private StringRedisTemplate redisTemplate;
	
	//获取数据
	public String get(String key){
		ValueOperations<String, String>  operations =redisTemplate.opsForValue();
		return operations.get(key);
	}
	
	//插入数据	
	public boolean set(String key,String value){
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		
		try {
			operations.set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//插入缓存定义超时时间为秒
	public boolean set(String key,String value,Long expireTime){
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value, expireTime, TimeUnit.DAYS);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 哨兵redis
	 */
	/*@Autowired
	private JedisSentinelPool jedisSentinelPool;
	
	public void set(String key,String value){
		Jedis jedis = jedisSentinelPool.getResource();
		jedis.set(key, value);
		jedisSentinelPool.returnResource(jedis);
	}
	
	public String get(String key){
		Jedis jedis = jedisSentinelPool.getResource();
		String result = jedis.get(key);
		jedisSentinelPool.returnResource(jedis);
		return result;		
	}*/
	
	
	
	/**
	 * 分片redis
	 */
	/*@Autowired
	private ShardedJedisPool jedisPool;
	
	public void set(String key,String value){
		ShardedJedis jedis = jedisPool.getResource();
		jedis.set(key, value);
	}
	
	public String get(String key){
		ShardedJedis jedis = jedisPool.getResource();
		return jedis.get(key);
	}*/
	/**
	 * 单个redis
	 */
	/*@Autowired
	private StringRedisTemplate redisTemplate;
	
	public void set(String key,String value){
		ValueOperations<String, String> operations=redisTemplate.opsForValue();
		operations.set(key, value);
	}
	
	public String get(String key){
		ValueOperations<String, String> operations=redisTemplate.opsForValue();
		return operations.get(key);
	}*/
}
