package es.parser.useragent.elastic;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.*;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


public class ElasticConnection {
	
	private TransportClient client = null;
	
	public ElasticConnection() {
	}
	
	@SuppressWarnings("resource")
	public TransportClient connect(String host, int port) throws Exception{
		
		try {
						
			Settings settings = Settings.builder().put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();
			client = new PreBuiltTransportClient(settings);
			client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

			
			System.out.println("[CREATE CONN] connection created!");
			return client;
		}catch(Exception e) {
			throw new Exception("[CREATE CONN] something happened node conn " + e.getMessage());
		}
		
		
	}
	
	public void closeConn(){
		if (client != null) {client.close();}
	}

}
