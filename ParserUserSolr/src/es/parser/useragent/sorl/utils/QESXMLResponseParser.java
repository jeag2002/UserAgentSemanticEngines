package es.parser.useragent.sorl.utils;

import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class QESXMLResponseParser extends XMLResponseParser {
    public QESXMLResponseParser() {
        super();
    }

    @Override
    public String getContentType() {
        return "text/html; charset=UTF-8";
    }
}