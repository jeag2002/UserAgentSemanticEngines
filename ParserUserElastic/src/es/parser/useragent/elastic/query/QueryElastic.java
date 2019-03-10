package es.parser.useragent.elastic.query;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class QueryElastic {
	
	private TransportClient client = null;
	private String indexName = "";
	private String indexTypeName = "";
	
	public QueryElastic(TransportClient _client, String _indexName, String _indexTypeName) {
		client = _client;
		indexName = _indexName;
		indexTypeName = _indexTypeName;
	}
	
	
	public String queryData(String query) {
		
		refreshIndices();
		return search(query);
	}
	
	
	private void refreshIndices(){
	     client.admin().indices().prepareRefresh(indexName).get(); 
	}
	    
	private String search(String query){
	    
		String result = "";
		
		//String queryWildcard = "*"+query+"*";
		Long startTime = System.currentTimeMillis();
	    
		//SearchResponse response = client.prepareSearch().get();
		MatchQueryBuilder filterQry = QueryBuilders.matchQuery("text", query);
		//SearchResponse response = client.prepareSearch(indexName).setTypes(indexTypeName).get();
		SearchResponse response = client.prepareSearch().setQuery(filterQry).execute().actionGet();
		
	    Long stopTime = System.currentTimeMillis();
	    
	    result = "Results query (" + query + ") numResults (" + response.getHits().getTotalHits() + ")  Time (" + (stopTime-startTime) + ") ms \n"; 
	    
	    
	    SearchHits dats = response.getHits();
	    SearchHit data[] = dats.getHits();
	    
	    for(int i=0; i<data.length; i++) {
	    	result += "___ " + data[i].toString() + "\n";
	    }
	    
	    return result;
	}
	
	

}
