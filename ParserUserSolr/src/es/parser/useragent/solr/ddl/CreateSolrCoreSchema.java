package es.parser.useragent.solr.ddl;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class CreateSolrCoreSchema {
	
	public static final String CORE_NAME = "user_agent";
	private static final String CONF_FOLDER = "conf";
	private static final String LANG_FOLDER = "lang";
	private static final String SCHEMA_FILE = "schema.xml";
	
	private static final String DEFAULT_SCHEMA_FILE = "managed-schema";
	
	
	
	private String path_to_backup = "";
	private String path_to_server = "";
	private String path_to_instance = "";
	private String path_to_scafold = "";
	
	private String path_to_instance_conf = "";
	private String path_to_instance_conf_lang = "";
	

	
	public CreateSolrCoreSchema() {
		path_to_backup = "./backup";
		path_to_server = "C:\\workspaces\\workSorlElastic\\servers\\solr-7.7.1\\server\\solr";
		path_to_scafold = "C:\\workspaces\\workSorlElastic\\servers\\solr-7.7.1\\server\\solr\\configsets\\_default\\conf";
		
	}
	
	public String createCoreRequest(String urlBase) throws Exception{
		
		SolrServer rem_server_conn = new HttpSolrServer(urlBase);
		
		CoreAdminRequest.Create core = new CoreAdminRequest.Create();
		core.setCoreName(CORE_NAME);
		core.setInstanceDir(path_to_instance);
		core.setSchemaName(SCHEMA_FILE);
		core.process(rem_server_conn);
		
		System.out.println("[CREATE_CORE] core (" + CORE_NAME + ") created");
		
		return CORE_NAME;
	}
	
	
	public String getSolrPath() {
		return path_to_server;
	}
	
	public boolean existCore(String urlBase, String core) throws Exception{
		
		SolrServer rem_server_conn = new HttpSolrServer(urlBase);
		
		// Request core list
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminAction.STATUS);
		CoreAdminResponse cores = request.process(rem_server_conn);

		// List of the cores
		List<String> coreList = new ArrayList<String>();
		for (int i = 0; i < cores.getCoreStatus().size(); i++) {
		    coreList.add(cores.getCoreStatus().getName(i));
		}
		
		return coreList.contains(core);
	}
	
	
	public void createSchema() throws Exception{
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.newDocument();
		
		Element rootElement = doc.createElement("schema");
		
		Attr attr = doc.createAttribute("name");
		attr.setValue(CORE_NAME);
		rootElement.setAttributeNode(attr);
		
		attr = doc.createAttribute("version");
		attr.setValue("1.6");
		rootElement.setAttributeNode(attr);
		
		Element field = doc.createElement("uniqueKey");
		field.setTextContent("id");
		rootElement.appendChild(field); 
		 
		field = doc.createElement("field");
		field.setAttribute("name", "id");
		field.setAttribute("type", "string");
		field.setAttribute("required", "true");
		field.setAttribute("indexed", "true");
		field.setAttribute("stored", "true");
		rootElement.appendChild(field);

		field = doc.createElement("field");
		field.setAttribute("name", "_text_");
		field.setAttribute("type", "text_basic");
		field.setAttribute("multiValued", "true");
		field.setAttribute("indexed", "true");
		field.setAttribute("stored", "true");
		field.setAttribute("docValues", "true");
		rootElement.appendChild(field);
		
		field = doc.createElement("dynamicField");
		field.setAttribute("name", "*");
		field.setAttribute("type", "text_basic");
		field.setAttribute("indexed", "true");
		field.setAttribute("stored", "false");
		rootElement.appendChild(field);
		
		field = doc.createElement("copyField");
		field.setAttribute("source", "*");
		field.setAttribute("dest", "_text_");
		rootElement.appendChild(field);
		
		
		field = doc.createElement("fieldType");
		field.setAttribute("name", "text_general");
		field.setAttribute("class", "solr.TextField");
		field.setAttribute("positionIncrementGap", "100");
		field.setAttribute("multiValued", "true");
		rootElement.appendChild(field);
		
		field = doc.createElement("fieldType");
		field.setAttribute("name", "booleans");
		field.setAttribute("class", "solr.BoolField");
		field.setAttribute("sortMissingLast", "true");
		field.setAttribute("multiValued", "true");
		rootElement.appendChild(field);
		 
		 //<fieldType name="pdates" class="solr.DatePointField" docValues="true" multiValued="true"/>
		field = doc.createElement("fieldType");
		field.setAttribute("name", "pdates");
		field.setAttribute("class", "solr.DatePointField");
	    field.setAttribute("docValues", "true");
		field.setAttribute("multiValued", "true");
		rootElement.appendChild(field);
		 
		 //<fieldType name="pdoubles" class="solr.DoublePointField" docValues="true" multiValued="true"/>
		field = doc.createElement("fieldType");
		field.setAttribute("name", "pdoubles");
	    field.setAttribute("class", "solr.DoublePointField");
		field.setAttribute("docValues", "true");
		field.setAttribute("multiValued", "true");
		rootElement.appendChild(field);
		
		field = doc.createElement("fieldType");
		field.setAttribute("name", "plongs");
		field.setAttribute("class", "solr.LongPointField");
		field.setAttribute("docValues", "true");
		rootElement.appendChild(field);
		
		
		field = doc.createElement("fieldType");
		field.setAttribute("name", "string");
		field.setAttribute("class", "solr.StrField");
		field.setAttribute("sortMissingLast", "true");
		field.setAttribute("docValues", "true");
		rootElement.appendChild(field);
		
		Element field_1 = doc.createElement("fieldType");
		field_1.setAttribute("name", "text_basic");
		field_1.setAttribute("class", "solr.SortableTextField");
		field_1.setAttribute("positionIncrementGap", "100");
		
		Element analyzer = doc.createElement("analyzer");
		
		Element tokenizer = doc.createElement("tokenizer");
		tokenizer.setAttribute("class", "solr.StandardTokenizerFactory");
		analyzer.appendChild(tokenizer);
		
		Element filter = doc.createElement("filter");
		filter.setAttribute("class", "solr.LowerCaseFilterFactory");
		analyzer.appendChild(filter);
		
		field_1.appendChild(analyzer);
		rootElement.appendChild(field_1);
		
		doc.appendChild(rootElement);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		StreamResult streamResult = new StreamResult(path_to_backup +  File.separatorChar + SCHEMA_FILE);
		transformer.transform(source, streamResult);
		
		
		Files.copy((new File(path_to_backup +  File.separatorChar + SCHEMA_FILE)).toPath(), (new File(path_to_instance_conf + File.separatorChar + SCHEMA_FILE)).toPath());
		
		System.out.println("[CREATE_CORE] create (" + SCHEMA_FILE + ") done");	
	}
		
	
	public String createCore(String urlBase) throws Exception{
		previousSteps();
		createSchema();
		createCoreRequest(urlBase);
		return CORE_NAME;
		
	}
	
	
	public void previousSteps() throws Exception{
		
		//1-Create folder:
		path_to_instance = path_to_server + File.separatorChar + CORE_NAME;
		boolean instance_created = (new File(path_to_instance)).mkdirs();
		if (!instance_created) {throw new Exception("[CREATE CORE] cannot create instance folder (" + path_to_instance + ")");}
		
		System.out.println("[CREATE_CORE] path to instance created (" + path_to_instance + ")");
		
		//2-Create subFolder
		path_to_instance_conf = path_to_instance + File.separatorChar + CONF_FOLDER;
		boolean conf_created = (new File(path_to_instance_conf)).mkdirs();
		if (!conf_created){throw new Exception("[CREATE CORE] cannot create instance conf folder (" + path_to_instance_conf + ")");} 
		
		path_to_instance_conf_lang = path_to_instance_conf + File.separatorChar + LANG_FOLDER;
		boolean lang_created = (new File(path_to_instance_conf_lang)).mkdirs();
		if (!lang_created){throw new Exception("[CREATE CORE] cannot create instance conf folder (" + path_to_instance_conf_lang + ")");} 
		
		
		
		//3-Copy content of scaffold to new folder
		File dir = new File(path_to_scafold);
		
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for(int i=0; i<files.length; i++) {
				if (files[i].isFile()) {
					Files.copy(files[i].toPath(), (new File(path_to_instance_conf+File.separatorChar+files[i].getName())).toPath());
				}
			}
		}
		
		dir = new File(path_to_scafold + File.separatorChar + LANG_FOLDER);
		
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for(int i=0; i<files.length; i++) {
				if (files[i].isFile()) {
					Files.copy(files[i].toPath(), (new File(path_to_instance_conf_lang+File.separatorChar+files[i].getName())).toPath());
				}
			}
		}
		
		System.out.println("[CREATE_CORE] copy data from (" + path_to_scafold + ") done");
		
		boolean deleteFile = (new File(path_to_instance_conf+File.separatorChar+this.DEFAULT_SCHEMA_FILE)).delete();
		
		if (!deleteFile) {throw new Exception("[CREATE CORE] remove default schema file (" + DEFAULT_SCHEMA_FILE + ")");}
		
		//4-Remove default xml_schema
		
		
		
		
	}
	
	
	
	
	
	

}
