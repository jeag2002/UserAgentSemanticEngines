package es.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyCustomInterceptor implements HandlerInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(MyCustomInterceptor.class);
	
	
	@Autowired
	@Qualifier("client")
	private TransportClient client;
	
	@Override
	public boolean preHandle
	      (HttpServletRequest request, HttpServletResponse response, Object handler) 
	      throws Exception {
	     
		 boolean resp = true;
		
		 try {
			 String user_agent = request.getHeader("User-Agent");
			 if (user_agent != null) {
				 logger.info("[MYCUSTOMINTERCEPTOR] user_agent (" + user_agent + ")");
				 resp = search(user_agent);
			 }
			 
			 
		 }catch(Exception e) {
			 logger.warn("[MYCUSTOMINTERCEPTOR] Something happened! " +  e.getMessage());
		 }
		 
		 return resp;
	}
	
	private boolean search(String query){
		Long startTime = System.currentTimeMillis();
		MatchQueryBuilder filterQry = QueryBuilders.matchQuery("text", query);
		//SearchResponse response = client.prepareSearch(indexName).setTypes(indexTypeName).get();
		SearchResponse response = client.prepareSearch().setQuery(filterQry).execute().actionGet();
	    SearchHits dats = response.getHits();
	    SearchHit data[] = dats.getHits();
	    return (data.length > 0);
	}
	
	
	
	 @Override
	   public void postHandle(HttpServletRequest request, HttpServletResponse response, 
	      Object handler, ModelAndView modelAndView) throws Exception {
	      
		 logger.info("[MYCUSTOMINTERCEPTOR] Post Handle method is Calling");
	   }
	 
	   @Override
	   public void afterCompletion
	      (HttpServletRequest request, HttpServletResponse response, Object 
	      handler, Exception exception) throws Exception {
	      
		   logger.info("[MYCUSTOMINTERCEPTOR] Request and Response is completed");
	   }
	
	 
	 
	
	 
	 
	 

}
