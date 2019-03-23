package es.parser.useragent.redis;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
	
	public static final String DEFAULT_REDISTANDALONEHOST = "localhost";
	public static final int DEFAULT_REDISTANDALONEPORT = 6379;
	
	private Jedis jediClient;
	private JedisPool jediPool;
	private JedisCluster jc;
	
	public RedisConnection() {
		
	}

	public Jedis getStandAloneJediConnection(String host, int port) throws Exception{
		jediClient = new Jedis(host, port);
		jediClient.connect();
		
		System.out.println("[REDIS CONNECTION] Standalone REDIS connection to (" + host + ":" + port + ") done sucessfully!");
		
		return jediClient;
	}
	
	
	public Jedis getPoolJediConnection(String host, int port) throws Exception{
		jediPool = new JedisPool(buildPoolConfig(),host, port);
	    jediClient = jediPool.getResource();
	    jediClient.connect();
	    System.out.println("[REDIS CONNECTION] Pool REDIS connection to (" + host + ":" + port + ") done sucessfully!");
	    return jediClient;
	}
	
	
	private JedisPoolConfig buildPoolConfig() {
	    final JedisPoolConfig poolConfig = new JedisPoolConfig();
	    poolConfig.setMaxTotal(128);
	    poolConfig.setMaxIdle(128);
	    poolConfig.setMinIdle(16);
	    poolConfig.setTestOnBorrow(true);
	    poolConfig.setTestOnReturn(true);
	    poolConfig.setTestWhileIdle(true);
	    poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
	    poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
	    poolConfig.setNumTestsPerEvictionRun(3);
	    poolConfig.setBlockWhenExhausted(true);
	    return poolConfig;
	}
	
	
	public JedisCluster getClusterJediConnection(String host, int port) throws Exception{
		
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort(host, port));
		jc = new JedisCluster(jedisClusterNodes);
		return jc;
		
	}
	
	
	public void closeConn() {
		
		if (jediClient != null) {jediClient.close();}
		if (jediPool != null) {jediPool.destroy();}
		
	}
	
	
	
}
