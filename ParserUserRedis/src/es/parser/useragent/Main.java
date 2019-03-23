package es.parser.useragent;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.parser.useragent.redis.RedisCVSData;
import es.parser.useragent.redis.RedisConnection;
import es.parser.useragent.redis.RedisQueryData;
import es.parser.useragent.redis.RedisRemoveAll;
import es.parser.useragent.utils.CSVProcessing;
import es.parser.useragent.utils.UnzipFilesUtils;
import redis.clients.jedis.Jedis;

public class Main {
	
	public static final String file_zip = "browscap-6000030.zip";
	public static final String csv_folder = "./backup";
	public static final String zip_folder = "./zip";
	
	public static final String console = "search>";
	public static final String quit = ":quit";
	
	private Jedis conn;
	
	
	private UnzipFilesUtils uFUtils;
	private RedisConnection rC;
	private RedisCVSData rCVS;
	private RedisQueryData rQD;
	private CSVProcessing csvP;
	private RedisRemoveAll rRA;
	
	
	public Main() throws Exception {
		uFUtils = new UnzipFilesUtils();
		rC = new RedisConnection();
		csvP = new CSVProcessing();
		rCVS = new RedisCVSData();
		rRA = new RedisRemoveAll();
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
	
	
	public void configure() throws Exception{
		
		conn = rC.getStandAloneJediConnection(rC.DEFAULT_REDISTANDALONEHOST, rC.DEFAULT_REDISTANDALONEPORT);
		
		rRA.removeAllKeys(conn);
		
		List<File> files = processFile();
		
		int index = 1;
		for(File fil: files) {
			fil = csvP.reformatFile(fil,index);
			rCVS.loadRedisData(conn, fil);
			index++;
		}
		
		rQD = new RedisQueryData(conn);
		
	}
	
	public int processCommands() throws Exception{
		int halt = 0;
		
		System.out.println("[MAIN] BEGIN!!");
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] REDIS searching console program");
		System.out.println("[MAIN] Commands:");
		System.out.println("[MAIN] search> :exact <criteria> look for exact key -- ej: :exact Ask");
		System.out.println("[MAIN] search> :aggr <criteria>,<criteria_1> look for more than one key -- ej: :aggr StumbleUpon,WinXP");
		System.out.println("[MAIN] search> :aprox <criteria> look for aprox key -- ej: :aprox MSIE");
		System.out.println("[MAIN] search> :range <limitinf>,<limitsup> look for range key -- ej: :range 2,10");
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
				System.out.print(rQD.results(line));
			}	
			
		}
		
		System.out.println("[MAIN] *********************************************************");
		System.out.println("[MAIN] bye!");
		return halt;
	}
	
	
	
	public void close() {
		rC.closeConn();
	}
	

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		int halt = 0;
		Main main = null;
		try {
			main = new Main();
			main.configure();
			halt = main.processCommands();
		}catch(Exception e) {
			System.out.println("[MAIN] Something happened " + e.getMessage());
			e.printStackTrace();
			halt = -1;
		}finally {
			if (main != null) {
				main.close();
			}
		}
		System.exit(halt);
	}

}
