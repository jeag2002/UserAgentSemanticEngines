package es.parser.useragent.elastic.bulk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


public class BulkCSVToElastic {
	
    public static final String indexName = "document";
    public static final String indexTypeName = "bulkindexing";
	
	public BulkCSVToElastic() {
	
	}
	
	public void processCSV(File file, TransportClient client) throws Exception{
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
       
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        int count=0,noOfBatch=1,totalCount=0;
        
      
       bufferedReader.readLine();
       bufferedReader.readLine();
        
        while ((line = bufferedReader.readLine())!=null){
           
            
            String data [] = line.split(",");
            try {
                    XContentBuilder xContentBuilder = jsonBuilder()
                            .startObject()
                            .field("id", String.valueOf(totalCount+1))
                            .field("text", data[0])
                            .endObject();
                   
                    bulkRequest.add(client.prepareIndex(indexName, indexTypeName, data[0])
                            .setSource(xContentBuilder));
                    if ((count+1) % 500 == 0) {
                        count = 0;
                        addDocumentToESCluser(bulkRequest, noOfBatch, count);
                        noOfBatch++;
                    }
            }catch (Exception e) {
            	throw new Exception("[BULK INDEXING] error while saving data " +  e.getMessage());
            }
    
            count++;
            totalCount++;
        }
        bufferedReader.close();
        addDocumentToESCluser(bulkRequest,noOfBatch,count);
        
        System.out.println("[BULK INDEXING] bulk (" + totalCount + ") records successfully!");		
		
	}
	
    private void addDocumentToESCluser(BulkRequestBuilder bulkRequest,int noOfBatch,int count){
        if(count==0){
            return;
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            int numberOfDocFailed = 0;
            Iterator<BulkItemResponse> iterator = bulkResponse.iterator();
            while (iterator.hasNext()){
                BulkItemResponse response = iterator.next();
                if(response.isFailed()){
                    //System.out.println("Failed Id : "+response.getId());
                    numberOfDocFailed++;
                }
            }
        }
        
        System.out.println("[BULK INDEXING] Bulk Indexing Completed for batch : "+noOfBatch);
        
    }
	
	

}
