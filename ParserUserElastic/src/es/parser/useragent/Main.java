package es.parser.useragent;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.elasticsearch.client.transport.TransportClient;

import es.parser.useragent.elastic.ElasticConnection;
import es.parser.useragent.elastic.bulk.BulkCSVToElastic;
import es.parser.useragent.elastic.query.QueryElastic;
import es.parser.useragent.utils.UnzipFilesUtils;

public class Main {
	
	public static final String URL_HOST = "localhost";
	public static final Integer URL_PORT = 8083;
	public static final String NODE_NAME = "user_agent";
	
	
	public static final String file_zip = "browscap-6000030.zip";
	public static final String csv_folder = "./backup";
	
	public static final String console = "search>";
	public static final String quit = ":quit";
	
	private UnzipFilesUtils uFUtils;
	private ElasticConnection conn;
	private BulkCSVToElastic bulk;
	private QueryElastic qElastic;
	
	private TransportClient client;
	
	
	public Main() {
		uFUtils = new UnzipFilesUtils();
		conn = new ElasticConnection();
		bulk = new BulkCSVToElastic();
	}
	
	
	public void configure() throws Exception{
		client = conn.connect(URL_HOST, URL_PORT);
		qElastic = new QueryElastic(client, bulk.indexName, bulk.indexTypeName);
		List<File> files = processFile();
		for(File csv: files) {bulk.processCSV(csv, client);}
	}
	
	public int processCommands() throws Exception{
		int halt = 0;
		
		System.out.println("[MAIN] BEGIN!!");
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] ELASTIC searching console program");
		System.out.println("[MAIN] Commands:");
		System.out.println("[MAIN] search> (criteria) found data in file processed");
		System.out.println("[MAIN] search> :quit exit of program");
		
		Scanner keyboard = new Scanner(System.in);
		boolean DONE = false;
		
		while(!DONE) {
			
			System.out.print(this.console);
			String line = keyboard.nextLine();
			
			if (line.indexOf(this.quit)!=-1) {
				DONE = true;
			}else {
				System.out.print(this.console);
				System.out.print(qElastic.queryData(line));
				
			}	
			
		}
		
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] bye!");
		
		
		return halt;
	}
	
	
	public void close(){
		conn.closeConn();
	}
	
	
	public List<File> processFile() throws Exception{
	 	
		   List<File> files = new ArrayList<File>();
			
		   InputStream in = Main.class.getClassLoader().getResourceAsStream(file_zip);
			
		   if (in != null) {
		    	File back = new File(csv_folder);
		    	files = new ArrayList<File>(uFUtils.unzipFiles(in, back));
		    	System.out.println("[PROCESS FILE] File processed and saved in " + csv_folder + " folder");
		    	
		    }else {
		    	throw new Exception("[PROCESS FILE] File (" + file_zip + ") not found");
		    }
		   
		    return files;
    }
	
	
	
	

	public static void main(String[] args) {
		
		int halt = 0;
		
		Main main = new Main();
		
		try {
			main.configure();
			halt = main.processCommands();
			
		}catch(Exception e) {
			System.out.println("[MAIN] Something happened " + e.getMessage());
			e.printStackTrace();
			halt = -1;
		}finally {
			main.close();
		}
		
		System.exit(halt);
		

	}

}
