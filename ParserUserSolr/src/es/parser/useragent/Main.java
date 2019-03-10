package es.parser.useragent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.parser.useragent.solr.ddl.CreateSolrCoreSchema;
import es.parser.useragent.solr.ddl.DeleteSolrCoreSchema;
import es.parser.useragent.sorl.SolrInterface;
import es.parser.useragent.sorl.index.ParserSolrCSV;
import es.parser.useragent.sorl.query.QuerySolrCoreSchema;
import es.parser.useragent.utils.RemoveHeadersCSV;
import es.parser.useragent.utils.UnzipFilesUtils;

public class Main {

	public static final String file_zip = "browscap-6000030.zip";
	public static final String csv_folder = "./backup";
	public static final String zip_folder = "./zip";
	
	public static final String console = "search>";
	public static final String quit = ":quit";
	
	private UnzipFilesUtils uFUtils;
	private CreateSolrCoreSchema cSCS;
	private DeleteSolrCoreSchema dSCS;
	private QuerySolrCoreSchema qSCS;
	
	private SolrInterface sI;
	private ParserSolrCSV parser;
	private RemoveHeadersCSV csv;
	
	private String core_name;
	
	public Main() throws Exception {
		uFUtils = new UnzipFilesUtils();
		cSCS = new CreateSolrCoreSchema();
		dSCS = new DeleteSolrCoreSchema();
		csv = new RemoveHeadersCSV();
	}
	
	private List<File> processFile() throws Exception{
	 	
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
	
	
	public void initializeSolr() throws Exception{
		
		if (cSCS.existCore(sI.connectionURL, cSCS.CORE_NAME)) {dSCS.deleteCoreSorl(sI.connectionURL, cSCS.CORE_NAME);}
		else {dSCS.deleteFolders(cSCS.getSolrPath(), cSCS.CORE_NAME);}
		
		core_name = cSCS.createCore(sI.connectionURL);
		sI = new SolrInterface(core_name);
		parser = new ParserSolrCSV(sI.getServer());
		qSCS = new QuerySolrCoreSchema(sI.getServer());
		
	}
	
	
	
	public void process() throws Exception{
		List<File> files = processFile();	
		parser.clear();
		for(File CSV: files) {
			csv.processCSV(CSV);
			parser.csvToSolr(CSV);
		}
	}
	
	
	public void finalizeSolr(String url, String core) {
		dSCS.deleteCoreSorl(url, core);
	}
	
	
	public String getBaseUrlMain() {
		return sI.connectionURL;
	}
	
	public String getCore() {
		return core_name;
	}
	
	
	public int processCommands() throws Exception{
		
		int halt = 0;
		
		System.out.println("[MAIN] BEGIN!!");
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] SOLR searching console program");
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
				System.out.print(qSCS.processQuery(line));			
			}	
		}
		
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] bye!");
		
		return halt;
	}
	
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		int halt = 0;
		
		Main main = new Main();
		
		try {
			
			main.initializeSolr();
			main.process();
			halt = main.processCommands();
		}catch(Exception e) {
			System.out.println("[MAIN] Something happened " + e.getMessage());
			e.printStackTrace();
			halt = -1;
		}finally {
			main.finalizeSolr(main.getBaseUrlMain(), main.getCore());
		}
		
		System.exit(halt);
	}

}
