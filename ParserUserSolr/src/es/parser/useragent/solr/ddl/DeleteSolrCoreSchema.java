package es.parser.useragent.solr.ddl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;

public class DeleteSolrCoreSchema {
	
	private HttpSolrServer server;
	
	public DeleteSolrCoreSchema() {
		
	}
	
	public void deleteFolders(String path, String core) throws Exception{
		
		String total_path = path+File.separatorChar+core;
		File fil = new File(total_path);
		if (fil.exists()) {
			Path pathToBeDeleted = (new File(total_path)).toPath();
		    Files.walk(pathToBeDeleted)
		      .sorted(Comparator.reverseOrder())
		      .map(Path::toFile)
		      .forEach(File::delete);
		}
	}
	
	
	public void deleteCoreSorl(String baseUrl, String core_name)  {
		
		try {
		
		SolrServer rem_server_conn = new HttpSolrServer(baseUrl);
		
		CoreAdminRequest.Unload core = new CoreAdminRequest.Unload(true);
		core.setCoreName(core_name);
		core.setDeleteDataDir(true);
		core.setDeleteIndex(true);
		core.setDeleteInstanceDir(true);
		core.process(rem_server_conn);
		
		System.out.println("[DELETE CORE] Core (" + core_name + ") deleted succesfully");
		
		}catch(Exception e) {
			System.out.println("[DELETE CORE] Something happened " + e.getMessage());
			//e.printStackTrace();
		}
		
		
	}
	

}
