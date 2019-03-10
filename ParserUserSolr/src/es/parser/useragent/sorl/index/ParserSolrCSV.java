package es.parser.useragent.sorl.index;

import java.io.File;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;

public class ParserSolrCSV {

	private HttpSolrServer server;
	
	public ParserSolrCSV(SolrServer solrServer) {
		server = (HttpSolrServer) solrServer;
		server.setConnectionTimeout(30000);
		server.setSoTimeout(10000);
		
		
	}
	
	public void clear() throws Exception{
		server.deleteByQuery("*:*");
		server.commit();
		
	}
	
	
	public void csvToSolr(File CSV) throws Exception{
		
		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update");
		up.addFile(CSV, "application/csv");
		up.setAction(ACTION.COMMIT, true, true);
		server.request(up);
		
		System.out.println("[LOAD DATA] data loaded to solr done!");
	}

}
