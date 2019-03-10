package es.springboot.configuration;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
	
	@Bean
	@Qualifier("client")
	public TransportClient connect() throws Exception{
		
		try {
						
			Settings settings = Settings.builder().put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();
			TransportClient client = new PreBuiltTransportClient(settings);
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
			logger.info("[CREATE CONN] connection created");
			return client;
		}catch(Exception e) {
			throw new Exception("[CREATE CONN] something happened node conn " + e.getMessage());
		}	
	}
	

}
