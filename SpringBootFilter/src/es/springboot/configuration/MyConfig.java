package es.springboot.configuration;

import java.io.IOException;
import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.*;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import es.springboot.interceptor.MyCustomInterceptor;

@SuppressWarnings("deprecation")
@Configuration
public class MyConfig extends WebMvcConfigurerAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(MyConfig.class);
	
	@Autowired
	MyCustomInterceptor myCustom;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(myCustom);
        logger.info("[MYCONFIG] attach interceptor");
    }
	
	

}
