package com.jt.test.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class TestRedis {
	//@Test
	public void test01(){
		Jedis jedis=new Jedis("192.168.179.134", 6379);
		jedis.set("name", "Kaka");
		System.out.println("redis中:"+jedis.get("name"));
	}
	
	/**
	 *分片测试
	 */
	//@Test
	public void test02(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setMaxIdle(30);
		
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.179.134", 6379));
		shards.add(new JedisShardInfo("192.168.179.134", 6380));
		shards.add(new JedisShardInfo("192.168.179.134", 6381));
		ShardedJedisPool jedisPool = new ShardedJedisPool(poolConfig, shards);
		ShardedJedis jedis = jedisPool.getResource();
		for(int i =1;i<=30;i++){
			jedis.set("KEY_"+i, i+"");
		}
		System.out.println("Redis insert complete...");
	}
	/**
	 * 哨兵测试
	 */
	@Test
	public void sentinel(){
		Set<String> sentinels = new HashSet<String>();
    	sentinels.add(new HostAndPort("192.168.179.134",26379).toString());
    	sentinels.add(new HostAndPort("192.168.179.134",26380).toString());
    	sentinels.add(new HostAndPort("192.168.179.134",26381).toString());
    	
    	//mymaster是在sentinel.conf中配置的名称
    	//sentinel monitor mymaster 192.168.179.134 6380 1
    	JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
    	System.out.println("当前master：" + pool.getCurrentHostMaster());
    	
    	Jedis jedis = pool.getResource();
		jedis.set("name", "Sylar");

    	System.out.println(jedis.get("name"));
    	pool.returnResource(jedis);   
    	
    	pool.destroy();
    	System.out.println("ok");
	}
}
