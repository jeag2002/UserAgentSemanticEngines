package es.parser.useragent.sorl.query;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class QuerySolrCoreSchema {
	
	private HttpSolrServer server;
	
	public QuerySolrCoreSchema(SolrServer solrServer) {
		server = (HttpSolrServer) solrServer;
		server.setConnectionTimeout(30000);
		server.setSoTimeout(10000);
	}
	
	public String processQuery(String inputData) throws Exception{
		
		String result = "";
		
		String queryStr = "_text_:"+inputData+"*"; 
		
		Long startTime = System.currentTimeMillis();
		
		SolrQuery query = new SolrQuery();
		query.set("q", queryStr);
		QueryResponse response = server.query(query);
		
		SolrDocumentList docList = response.getResults();
	
		Long stopTime = System.currentTimeMillis();
	
		result = "Results query (" + queryStr + ") numResults (" + docList.size() + ")  Time (" + (stopTime-startTime) + ") ms \n";
		
		for(int i=0; i<docList.size(); i++) {
			result += "___ " + docList.get(i).toString() + "\n";
		}
		return result;
	}

}
