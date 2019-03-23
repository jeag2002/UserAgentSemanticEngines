package es.parser.useragent.redis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class RedisCVSData {
	
	private static final int LIMIT = 1000;
	
	
	public void loadRedisData(Jedis jedi, File fil) throws Exception{
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fil));
		
		String line = "";
		
		Pipeline pipelined = jedi.pipelined();
		
		int limit = 0;
		int numrecords = 0;
		
		while ((line = bufferedReader.readLine())!=null){
			
			String data[] = line.split(",");
			
			if (data != null) {
				
				String key = "index:"+data[0];
				Map<String,String> values = new HashMap<String,String>();
				
				String keyParent = "";
				String keyParent_1 = "";
				
				if (data.length == 4) {
					values.put("name",data[1]);
					values.put("parent", "no-data");
					values.put("so", "no-data");
					
					keyParent = "no-data";
					keyParent_1 = "no-data";
					
				}else if (data.length == 5) {
					values.put("name", data[1]);
					values.put("parent", data[4]);
					values.put("so", "no-data");
					
					keyParent = data[4];
					keyParent_1 = "no-data";
				
				}else if (data.length == 6) {
					
					values.put("name", data[1]);
					values.put("parent", data[4]);
					values.put("so", data[5]);
					
					keyParent = data[4];
					keyParent_1 = data[5];
					
				}
				
				pipelined.hmset(key, values);
				
				pipelined.sadd("name:"+data[1],data[0]);
				pipelined.sadd("parent:"+keyParent, data[0]);
				pipelined.sadd("so:"+keyParent_1, data[0]);
				
				pipelined.sadd("user_agent",data[0] +"__"+ data[1]);
				pipelined.zadd("index_agent",Double.parseDouble(data[0]),data[0]+"__"+data[1]);
				
				
			}
			
			limit++;
			numrecords++;
			if (limit >= LIMIT) {
				pipelined.sync();
				limit = 0;
			}
			
		}
		
		if (limit > 0) {pipelined.sync();}
		bufferedReader.close();
		
		System.out.println("[REDIS BULK DATA] inserted (" + numrecords + ") records correctly!");
		
	}
	

}
