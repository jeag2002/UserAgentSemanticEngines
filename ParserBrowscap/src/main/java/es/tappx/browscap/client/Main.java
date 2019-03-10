package es.tappx.browscap.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//https://github.com/blueconic/browscap-java

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;


public class Main {
	
	public Main() {
		
	}
	
	
	
	public void process(String query) throws Exception{
		
		long iniTest = System.currentTimeMillis();
		
		List<BrowsCapField> datafilter = Arrays.asList(
				BrowsCapField.BROWSER, 
				BrowsCapField.BROWSER_TYPE,
                BrowsCapField.BROWSER_MAJOR_VERSION,
                BrowsCapField.DEVICE_TYPE, 
                BrowsCapField.DEVICE_BRAND_NAME,
                BrowsCapField.PLATFORM, 
                BrowsCapField.PLATFORM_VERSION,
                BrowsCapField.RENDERING_ENGINE_VERSION, 
                BrowsCapField.RENDERING_ENGINE_NAME,
                BrowsCapField.PLATFORM_MAKER, 
                BrowsCapField.RENDERING_ENGINE_MAKER);
		
		
		final UserAgentParser parser = new UserAgentService().loadParser(datafilter);
		final Capabilities capabilities = parser.parse(query);
		
		Map<BrowsCapField,String> data = capabilities.getValues();
		
		System.out.println("============= RESULTS query:("+query+")============= [INI]");
		
		for (BrowsCapField name: data.keySet()){

            String key = name.toString();
            String value = data.get(name).toString();  
            System.out.println(key + " " + value);  


		} 
		
		long test = System.currentTimeMillis() - iniTest;
		
		System.out.println("============= RESULTS query:("+query+")============= [END]___TIME(" + test + ") ms");
		
	}
	
	
	public void process(String input_zip_path, String query) throws Exception{
		
		
		long iniTest = System.currentTimeMillis();
		
		List<BrowsCapField> datafilter = Arrays.asList(
				BrowsCapField.BROWSER, 
				BrowsCapField.BROWSER_TYPE,
                BrowsCapField.BROWSER_MAJOR_VERSION,
                BrowsCapField.DEVICE_TYPE, 
                BrowsCapField.DEVICE_BRAND_NAME,
                BrowsCapField.PLATFORM, 
                BrowsCapField.PLATFORM_VERSION,
                BrowsCapField.RENDERING_ENGINE_VERSION, 
                BrowsCapField.RENDERING_ENGINE_NAME,
                BrowsCapField.PLATFORM_MAKER, 
                BrowsCapField.RENDERING_ENGINE_MAKER);
		
		
		final UserAgentParser parser = new UserAgentService(input_zip_path).loadParser(datafilter);
		final Capabilities capabilities = parser.parse(query);
		
		Map<BrowsCapField,String> data = capabilities.getValues();
				
		
		System.out.println("============= RESULTS path:("+input_zip_path+") query:("+query+") ============= [INI]");
		
		for (BrowsCapField name: data.keySet()){

            String key = name.toString();
            String value = data.get(name).toString();  
            System.out.println(key + " " + value);  


		} 
		
		long test = System.currentTimeMillis() - iniTest;
		
		System.out.println("============= RESULTS path:("+input_zip_path+") query:("+query+")============= [END]___TIME(" + test + ") ms");
		
		
		
		
	}
	

	public static void main(String[] args) throws Exception{
		
		Main main = new  Main();
		
		if (args.length < 1) {
			System.out.println("Expect at least two parameter");
			System.out.println("Main <path_to_zip_browscap> <query>");
			
		}else if (args.length == 1) {	
			
			String query = args[0];
			main.process(query);
			
		}else {
			String input_zip_path  = args[0];
			String query = args[1];
			main.process(input_zip_path, query);
		}

	}

}
