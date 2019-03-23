package es.parser.useragent.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class RedisQueryData {
	
	private Jedis jedisConn;
	
	private static final String EXACT = ":exact";
	private static final String AGGR = ":aggr";
	private static final String APROX = ":aprox";
	private static final String RANGE = ":range";
	
	
	public RedisQueryData(Jedis conn) throws Exception{
		jedisConn = conn;
	}
	
	
	public String results(String inputQuery) throws Exception{
		
		if (inputQuery.indexOf(EXACT) != -1) {
			String query = inputQuery.substring(inputQuery.indexOf(EXACT) + EXACT.length());
			return exactResults(query.trim());
		}else if (inputQuery.indexOf(APROX) != -1) {
			String query = inputQuery.substring(inputQuery.indexOf(APROX) + APROX.length());
			return aproxResults(query.trim());
		}else if (inputQuery.indexOf(AGGR) != -1) {
			String query = inputQuery.substring(inputQuery.indexOf(AGGR) + AGGR.length());
			query = query.trim();
			String data[] = query.split(",");	
			if (data.length == 2) {
				return aggrResults(data[0],data[1]);
			}else {
				return "query not found (" + inputQuery + ")\r\n";
			}
		}else if (inputQuery.indexOf(RANGE) != -1) {
			
			String query = inputQuery.substring(inputQuery.indexOf(RANGE) + RANGE.length());
			query = query.trim();
			String data[] = query.split(",");	
			if (data.length == 2) {
				return rangeResults(data[0],data[1]);
			}else {
				return "query not found (" + inputQuery + ")\r\n";
			}
			
			
			
			
		}else {
			return "query not found (" + inputQuery + ")\r\n";
		}
	}
	
	
	
	public String rangeResults(String data_1, String data_2) throws Exception{
		
		String result = "";
		
		Long startTime = System.currentTimeMillis();
		
		Long start = Long.parseLong(data_1);
		Long stop = Long.parseLong(data_2);
		if (stop < start) {new Exception ("Unaceptable indexes");}
		Set<String> results = jedisConn.zrange("index_agent", start, stop);
		
		Long stopTime = System.currentTimeMillis(); 
		
		result = "Results query by range key index_agent ("+start+"," + stop + ") Time (" + (stopTime-startTime) + ") ms\r\n";
		
		
		if (results != null) {
			ArrayList<String> dataList = new ArrayList<String>(results);
			
			if (dataList.size() > 0) {
				for(String item: dataList) {
					result += "____ " + item + "\r\n";
				}
			}else {
				result += "Query empty!\r\n";
			}
			
		}else {
			result += "Query empty!\r\n";
		}
		
		return result;
		
		
		
		
	}
	
	
	
	
	
	
	public String aggrResults(String data_1, String data_2) throws Exception{
		String result = "";
		
		Long startTime = System.currentTimeMillis();
		Set<String> results = jedisConn.sinter("parent:"+data_1,"so:"+data_2);
		Long stopTime = System.currentTimeMillis(); 
		
		result = "Results query by aggr key (parent:" + data_1 + ", so: " + data_2 + ") Time (" + (stopTime-startTime) + ") ms\r\n";
		
		if (results != null) {
			ArrayList<String> dataList = new ArrayList<String>(results);
			
			if (dataList.size() > 0) {
				for(String item: dataList) {
					result += "____ " + item + "\r\n";
				}
			}else {
				result += "Query empty!\r\n";
			}
			
		}else {
			result += "Query empty!\r\n";
		}
		
		return result;
	}
	
	
	
	
	
	
	public String exactResults(String inputQuery) throws Exception{
		String result = "";
		
		Long startTime = System.currentTimeMillis();
		
		Set<String> results = jedisConn.smembers("parent:"+inputQuery);
		
		Long stopTime = System.currentTimeMillis();
		 
		result = "Results query by exact key (parent:" + inputQuery + ") Time (" + (stopTime-startTime) + ") ms\r\n";
		
		if (results != null) {
			ArrayList<String> dataList = new ArrayList<String>(results);
			
			if (dataList.size() > 0) {
				for(String item: dataList) {
					result += "____ " + item + "\r\n";
				}
			}else {
				result += "Query empty!\r\n";
			}
			
		}else {
			result += "Query empty!\r\n";
		}
		
		return result;
	}
	
	
	public String aproxResults(String inputQuery) throws Exception{
	   
		String result = "";
		
		String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START; 
		
		ScanParams scanParams = new ScanParams().match("*"+inputQuery+"*").count(10);
		
		String items = "";
		
		boolean cycleIsFinished = false;
	
		long startTime = System.currentTimeMillis();
		
		int numItems = 0;
		
		while(!cycleIsFinished) {
			  
			  ScanResult<String> scanResult	= jedisConn.sscan("user_agent", cur, scanParams);
			  List<String> info = scanResult.getResult();
			  
			  for(String data: info) {
				  items += "____ " + data + "\r\n";
			  }
			  
			  
			  cur = scanResult.getCursor();
			  if (cur.equals("0")) {
			    cycleIsFinished = true;
			  }
			  
			  numItems+=info.size();
			  if (numItems > 20){cycleIsFinished = true;}
			  
		}  
		long stopTime = System.currentTimeMillis();
		
		result += "Results query by aprox key (name:" + inputQuery + ") Time (" + (stopTime-startTime) + ") ms\r\n";
		
		if (items.equalsIgnoreCase("")) {
			result += "Query empty!\r\n";
		}else {
			result += items;
		}
		
		return result;
	}

}
