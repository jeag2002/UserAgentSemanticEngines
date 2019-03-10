package es.parser.useragent.sorl;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

import es.parser.useragent.sorl.utils.QESXMLResponseParser;

public class SolrInterface {
	
	//public static final String connectionURL = "http://localhost:8082/solr/useragent";
	
	public static final String connectionURL = "http://localhost:8082/solr";
	
	private HttpSolrServer server;
	
	public SolrInterface(String core) throws Exception{
		
		server = new HttpSolrServer(connectionURL + "/" + core);
		server.setMaxRetries(1);
		server.setConnectionTimeout(5000);
		server.setParser(new XMLResponseParser());
		server.setSoTimeout(1000);
		server.setDefaultMaxConnectionsPerHost(100);
		server.setMaxTotalConnections(100);
		server.setFollowRedirects(false);
		server.setAllowCompression(true);
		
		System.out.println("[SERVER CONNECT] Server connected to (" + connectionURL + ")");
	}
	
	public SolrServer getServer() {
		return server;
	}
	
	

}
