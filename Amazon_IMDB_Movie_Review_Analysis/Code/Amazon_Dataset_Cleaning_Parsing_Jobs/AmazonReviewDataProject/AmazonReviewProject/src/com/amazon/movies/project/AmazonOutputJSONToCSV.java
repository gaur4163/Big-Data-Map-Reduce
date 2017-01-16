package com.amazon.movies.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AmazonOutputJSONToCSV {

	public static void main(String args[]) {
		// TODO Auto-generated method stub
				 PrintWriter writer = null;
				try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\inigam\\Documents\\Semester\\Amazon-Movie-Review-Analysis-master\\out\\disk1out.txt"))) {
					writer = new PrintWriter("C:\\Users\\inigam\\Documents\\Semester\\Amazon-Movie-Review-Analysis-master\\out\\disk1out.csv");
					   
					String line;
				    JSONParser parser = new JSONParser();
				    int count =0;
//				    
				    /**
				     * Released":"31 Jul 2009",
						"Metascore":"N\/A",
						"imdbID":"tt1286871","
						Plot":"A documentary about the first woman to join the Indian Police Service.",
						"+":"Megan Doneman","
						Title":"Kiran Bedi: Yes Madam, Sir",
						"Actors":"Kiran Bedi, Helen Mirren",
						"imdbRating":"8.9","
						imdbVotes":"85",
						"Response":"True",
						"AmazonAvgScore":"2.925925925925926",
						"Runtime":"95 min",
						"Type":"movie"
						,"Awards":"2 wins.","
						Year":"2008",
						"Language":"Hindi,English",
						"Rated":"N\/A",
						"Poster":"http:\/\/ia.media-imdb.com\/images\/M\/MV5BMTUwODAyMzk3MV5BMl5BanBnXkFtZTgwNjA4MjA2MDE@._V1_SX300.jpg",
						"Country":"Australia, India",
						"AmazonTitle":"Yes, Madam!",
						"AmazonID":"B000009HII","
						Genre":"Documentary",
						"Writer":"Megan Doneman, Jeneffa Soldatic (story)"
						}
				      */
					Pattern p = Pattern.compile("-?\\d+");
			    	Pattern oscarRegEx = Pattern.compile("(^['Nominated']+\\s+['for']+)+\\s*\\d+\\s*['Oscar']+");
			    	
				    while ((line = br.readLine()) != null) {
				    	JSONObject obj = (JSONObject) parser.parse(line);
				    	long imdbVote= 0;
				    	try{
				    		imdbVote=Long.parseLong(((String) obj.get("imdbVotes")).replaceAll(",",""));
				    	}catch(NumberFormatException ex){
				    		imdbVote= 0;
				    	}
				    	
				    
				    	Matcher m = p.matcher((String) obj.get("Runtime"));
				    	long runtime=0;
				    	while(m.find()){
				    		runtime= Long.parseLong(m.group());
				    		break;
				    	}
				    	
				    	String oscarAwards = (String) obj.get("Awards");
				    	int oscarNominated=0;
				    	int oscarWinner=0;
				    	int otherAwards=0;
				    	String beforeOscar=null;
				    	String afterOscar=null;
				    	try{
					    		if((oscarAwards.toLowerCase()).contains("oscar")){
					    			 beforeOscar= (oscarAwards.toLowerCase()).split("oscar")[0];
					    			 afterOscar= (oscarAwards.toLowerCase()).split("oscar")[1];
					    		}else{
					    			afterOscar=oscarAwards;
					    		}
				    		
				    			if(beforeOscar!=null){
				    				if(beforeOscar.toLowerCase().contains("nominat")){
				    					beforeOscar=beforeOscar.replaceAll("[^0-9]+"," ");
				    					oscarNominated=Integer.parseInt(beforeOscar.trim());
				    				}else{
				    					oscarWinner=Integer.parseInt(beforeOscar.replaceAll("[^0-9]+"," ").trim());
				    				}
				    			}
				    			if(afterOscar!=null){
				    				afterOscar = afterOscar.replaceAll("[^0-9]+"," ");
				    				String[] intArr= afterOscar.trim().split(" ");
				    				int sum =0;
				    				for(String val:intArr){
				    					sum =sum+Integer.parseInt(val);
				    				}
				    				otherAwards=sum;
				    				sum=0;
				    				//System.out.println(afterOscar.trim().split(" ")[0]+afterOscar.trim().split(" ")[1]);
				    			}
				    		
				    	}catch(Exception e){
				    		
				    	}
//				    	try{
//					    	System.err.println("nominated start");
//					    	if((oscarAwards.toLowerCase()).contains("oscar")){
//					    		System.err.println("nominated oscar start");
//					    		Matcher oscarNominatedMatcher = oscarRegEx.matcher(oscarAwards.toLowerCase());
//					    		System.out.println("finder nominated"+oscarNominatedMatcher.find());
//					    		while(oscarNominatedMatcher.find()){
//					    			String temp= oscarNominatedMatcher.group();
//					    			System.err.println("nominated"+temp);
//					    			temp=temp.replaceAll("^\\d", "");
//					    			oscarNominated= Integer.parseInt(temp.trim());
//						    		break;
//						    	}
//					    	}
//				    	}catch(Exception e){
//				    		
//				    		e.printStackTrace();
//				    		 oscarNominated=0;
//				    	}
//				    	try{
//					    	String oscarWinnerPattern ="/((^['Won']+)+\\s*\\d+\\s*(['oscar']+))/ig";
//					    	Pattern oscarWinnerRegEx = Pattern.compile(oscarWinnerPattern);
//					    	if((oscarAwards.toLowerCase()).contains("oscar")){
//					    		Matcher oscarWinnerMatcher = oscarWinnerRegEx.matcher(oscarAwards);
//					    		while(oscarWinnerMatcher.find()){
//					    			String temp= oscarWinnerMatcher.group();
//					    			temp=temp.replaceAll("^\\d", "");
//					    			oscarWinner= Integer.parseInt(temp.trim());
//						    		break;
//						    	}
//					    	}
//				    	}catch(Exception e){
//				    		e.printStackTrace();
//				    		 oscarWinner=0;
//				    	}
				    	
				    	/*try{
					    	String oscarWinnerPattern ="/((^['Won']+)+\\s*\\d+\\s*(['oscar']+))/ig";
					    	Pattern oscarWinnerRegEx = Pattern.compile(oscarWinnerPattern);
					    	if(oscarAwards.contains("oscar")){
					    		Matcher oscarWinnerMatcher = oscarWinnerRegEx.matcher(oscarAwards);
					    		while(oscarWinnerMatcher.find()){
					    			String temp= oscarWinnerMatcher.group();
					    			temp=temp.replaceAll("^\\d", "");
					    			oscarWinner= Integer.parseInt(temp.trim());
						    		break;
						    	}
					    	}
				    	}catch(Exception e){
				    		 oscarWinner=0;
				    	}*/
				    	String result = ((String) obj.get("Released")).replaceAll(",","\t")+","+((String) obj.get("imdbID")).replaceAll(",", "\t")+","+((String) obj.get("Director")).replaceAll(",","\t")+","+((String) obj.get("Title")).replaceAll(",","\t")+","+((String) obj.get("Actors")).replaceAll(",","\t")+","+((String) obj.get("imdbRating")).replaceAll(",","\t")+","+(imdbVote)+","+((String) obj.get("AmazonAvgScore")).replaceAll(",","\t")+","+runtime+","+((String) obj.get("Awards")).replaceAll(",","\t")+","+((String) obj.get("Year")).replaceAll(",","\t")+","+((String) obj.get("Language")).replaceAll(",","\t")+","+((String) obj.get("Country")).replaceAll(",","\t")+","+((String) obj.get("AmazonTitle")).replaceAll(",","\t")+","+((String) obj.get("AmazonID")).replaceAll(",","\t")+","+((String) obj.get("Genre")).replaceAll(",","\t")+","+((String) obj.get("Writer")).replaceAll(",","\t")+","+((String) obj.get("Poster")).replaceAll(",","")+","+oscarWinner+","+oscarNominated+","+otherAwards;
				    	writer.println(result);
				    	 //count++;
				    	//if(count>10){
				    	//	break;
				    	//}
				    }
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					 writer.close();
				}


	}

}
