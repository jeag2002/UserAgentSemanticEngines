package es.parser.useragent.redis;

import redis.clients.jedis.Jedis;

public class RedisRemoveAll {
	
	public void removeAllKeys(Jedis conn) throws Exception{
		try {
			String result = conn.flushDB();
			System.out.println("[REDIS REMOVE ALL] Remove all keys (" + result + ")");
		}catch(Exception e) {
			throw e;
		}
		
	}

}
