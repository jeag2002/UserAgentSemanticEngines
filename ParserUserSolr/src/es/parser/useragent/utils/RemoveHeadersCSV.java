package es.parser.useragent.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.internal.csv.CSVParser;
import org.apache.solr.internal.csv.CSVPrinter;
import org.apache.solr.internal.csv.CSVStrategy;

public class RemoveHeadersCSV {
	
	public RemoveHeadersCSV() {
	
	}
	
	public void processCSV(File fil) throws Exception{
		
		
		FileReader reader = new FileReader(fil);
		CSVParser parser = new CSVParser(reader);
		
		String[][] info = parser.getAllValues();
		
		
		FileWriter writer = new FileWriter(fil);
		CSVPrinter printer = new CSVPrinter(writer, CSVStrategy.DEFAULT_STRATEGY);
		
		
		Matrix max = new Matrix(info);
		
		String[][] subinfo = max.getSubMatrix(2, 0, 1, info.length-2).getData();
	
		for(int i=2;i<subinfo.length; i++) {
			String[] row = subinfo[i];
			
			List<String> data = Arrays.asList(row);
			List<String> response = new ArrayList<String>();
			response.add(String.valueOf(i-2));
			response.addAll(data);
			
			String[] data_type = new String[2];
			printer.println(response.toArray(data_type));
		}
		
		printer.flush();
		
		System.out.println("[REMOVE HEADER] File (" +  fil.getPath() + ")  registers (" + info.length  + ") processed");
		
		
		
		
	}

}
