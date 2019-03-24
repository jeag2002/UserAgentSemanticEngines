package es.parser.useragent;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.parser.useragent.csv.CSVProcessing;
import es.parser.useragent.lucene.TextFileIndexer;
import es.parser.useragent.utils.UnzipFilesUtils;

public class Main {
	
	private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	
	public static final String file_zip = "browscap-6000030.zip";
	public static final String csv_folder = "./backup";
	public static final String index_folder = "./index";
	
	public static final String console = "search>";
	public static final String quit = ":quit";
	
	private UnzipFilesUtils uFUtils;
	private TextFileIndexer textFileIndexer;
	private CSVProcessing csv;
	
	
	public Main() throws Exception{
		uFUtils = new UnzipFilesUtils();
		textFileIndexer = new TextFileIndexer(index_folder);
		csv = new CSVProcessing();
		
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
	
	
	
	public void configure() throws Exception{
		List<File> files = processFile();
		int index = 1;
		for(File csvFile: files) {
			File response = csv.reformatFile(csvFile, index);
			textFileIndexer.indexFileOrDirectory(response.getPath());
			index++;
		}
		textFileIndexer.closeIndex();	
	}
	
	
	
	private String getResults(String query, IndexReader reader, IndexSearcher searcher, TopScoreDocCollector collector) throws Exception{
		String results = "";
	
		Long startTime = System.currentTimeMillis();
		
		Query q =  new QueryParser(Version.LUCENE_40, "contents", analyzer).parse(query);
		searcher.search(q, collector);
		
		Long stopTime = System.currentTimeMillis();
		
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		results = "Results query (" + query + ") numResults (" + hits.length + ")  Time (" + (stopTime-startTime) + ") ms \n"; 
		
		for(int i=0; i<hits.length; i++) {
			results += "___ " + hits[i].toString() + "\n";
		}
	
	
		return results;
	
	}
	
	
	
	public int processCommands() throws Exception{
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index_folder)));
	    IndexSearcher searcher = new IndexSearcher(reader);
		
		
		int halt = 0;
		
		System.out.println("[MAIN] BEGIN!!");
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] LUCENE searching console program");
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
				TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
				System.out.print(this.console);
				System.out.print(getResults(line, reader, searcher, collector));
			}	
			
		}
		
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] bye!");
		
		
		return halt;
	}
	
	
	
	

	public static void main(String[] args) {
		
		int halt = 0;
		
		try {
			Main main = new Main();
			main.configure();
			halt = main.processCommands();
		}catch(Exception e) {
			System.out.println("[MAIN] Something happened " + e.getMessage());
			e.printStackTrace();
			halt = -1;
		}finally {
			
		}
		
		System.exit(halt);

	}

}
