package es.parser.useragent.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;



public class CSVProcessing {
	
	public CSVProcessing() {
		
	}

	public File reformatFile(File fil, int index) throws Exception{
		
		 File fileResponse = new File(fil.getParentFile().toString() + File.separatorChar + "CSVProcessed_" +  String.valueOf(index) + ".csv");
		 
		 Reader reader = Files.newBufferedReader(fil.toPath());
		 BufferedWriter writer = Files.newBufferedWriter(fileResponse.toPath());
		 
		 CSVParser csvParser =  new CSVParser(reader, CSVFormat.DEFAULT);
		 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
		 
		 List<CSVRecord> lists = csvParser.getRecords();
		 
		 for(int i=2; i<lists.size(); i++) {
			 CSVRecord record = lists.get(i);
			 csvPrinter.printRecord(String.valueOf(i-2),record.get(0)); 
		 }	 
		 csvPrinter.flush();
		 
		 return fileResponse;
	}
	
}
