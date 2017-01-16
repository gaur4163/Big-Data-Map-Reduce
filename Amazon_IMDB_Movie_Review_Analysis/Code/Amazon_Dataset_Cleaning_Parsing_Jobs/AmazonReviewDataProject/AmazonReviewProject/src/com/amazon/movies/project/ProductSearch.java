package com.amazon.movies.project;


import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazon.movies.project.SignedRequestHelper;

/*
 * This class shows how to make a simple authenticated call to the
 * Amazon Product Advertising API.
 *
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class ProductSearch {

    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAIUC2TDK4O7F6IPZA";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "DRk9BoIlStxz3DxSu7zTjG1Zn7lI0UDIssrH9/Bi";

    /*
     * Use the end-point according to the region you are interested in.
     */
    private static final String ENDPOINT = "webservices.amazon.com";
    SignedRequestHelper helper;
    public ProductSearch(){
        /*
         * Set up the signed requests helper.
         */
        try {
            helper = SignedRequestHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getMovieDetails(String prodids) throws IOException, SAXException, ParserConfigurationException, ParseException, InterruptedException {

    
        String requestUrl = null;

        Map<String, String> params = new HashMap<String, String>();

        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemLookup");
        params.put("AWSAccessKeyId", "AKIAIUC2TDK4O7F6IPZA");
        params.put("AssociateTag", "amazonreviewt-20");
        params.put("ItemId", prodids);
        params.put("IdType", "ASIN");
        params.put("ResponseGroup", "ItemAttributes");

        requestUrl = helper.sign(params);

      /* System.out.println("Signed URL: \"" + requestUrl + "\"");
      */  
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;
        boolean notSuccess=true;
        while(notSuccess){
        	try{
        		 doc = db.parse(new URL(requestUrl).openStream());
        		// System.out.println(doc);
        		 notSuccess=false;
        		 break;
        	}
        	catch(Exception e){
        		System.out.println(e);
        	   
        	}
        }
        
        NodeList node=null;
        try{
         node = doc.getElementsByTagName("Title");
         return node.item(0).getTextContent();
        }
        catch(Exception e){
        	
        }
      return null;
    }
}


