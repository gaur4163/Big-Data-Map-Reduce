package job4;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* 
 * receives ((MivieId_1, MovieId_2), list of (Ratings_1, Ratings_2))
 * emits ((MovieId_1, MovieId_2), similarity)
 * sample input: ((8,235), (5.0;6;25,3.0;3;8)) 
 * sample output: ((8,235), similarity) 
 * */

public class Job4Reducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		try{
			
		String [] m1_det = key.toString().split(":");
		String m1 = m1_det[0];
		

		
		String[] features_m1 = m1_det[1].split(",");
		
		String director1	= features_m1[0].trim();
		String actor1	= features_m1[1].trim();
		double imdbRating1	= Double.parseDouble(features_m1[2].trim());
		int imdbVote1	= Integer.parseInt(features_m1[3].trim());
		double amazonAvgScore1 = Double.parseDouble(features_m1[4].trim());
		int year1	= Integer.parseInt(features_m1[5].trim());
		String language1	= features_m1[6].trim();
		String country1	= features_m1[7].trim();
		String genre1	= features_m1[8].trim();
		int oscarWinner1	= Integer.parseInt(features_m1[9].trim());
		int oscarNominated1	= Integer.parseInt(features_m1[10].trim());
		int otherAwards1 = Integer.parseInt(features_m1[11].trim());
		

		for (Text value : values) {
			try{
				
			String[] m2_det = value.toString().split(":");
			
			
			String m2 = m2_det[0];
			String[] features_m2 = m2_det[1].split(",");
			String director2	= features_m2[0].trim();
			String actor2	= features_m2[1].trim();
			double imdbRating2	= Double.parseDouble(features_m2[2].trim());
			int imdbVote2	= Integer.parseInt(features_m2[3].trim());
			double amazonAvgScore2 = Double.parseDouble(features_m2[4].trim());
			int year2	= Integer.parseInt(features_m2[5].trim());
			String language2	= features_m2[6].trim();
			String country2	= features_m2[7].trim();
			String genre2	= features_m2[8].trim();
			int oscarWinner2	= Integer.parseInt(features_m2[9].trim());
			int oscarNominated2	= Integer.parseInt(features_m2[10].trim());
			int otherAwards2 = Integer.parseInt(features_m2[11].trim());
			String [] actors2 = actor2.split(" ");
			String [] languages2 = language2.split(" ");
			
			double sumOfMatchingAttributes =0;
			
			//all categorical features first
			
			if(director2.equals(director1))
			{sumOfMatchingAttributes +=1; 
			
			}
			
			
			for(String s: actors2)
			{
				if(actor1.matches(".*\\b"+s+"\\b.*")){
					sumOfMatchingAttributes +=1;
					break;
				}
			}
			
			for(String s: languages2)
			{
				if(language1.matches(".*\\b"+s+"\\b.*")){
					sumOfMatchingAttributes +=1;
					break;
				}
			}
			
			
			if(country2.equals(country1))
			{sumOfMatchingAttributes +=1; }
		
			
			if(genre2.equals(genre1))
			{sumOfMatchingAttributes +=1; }
			
		
			
			if(year2==year1)
			{sumOfMatchingAttributes +=1; }
			
			double diff=0;
			
			
			
			//now all numerical
			if(imdbRating1>imdbRating2)
			 diff = imdbRating1-imdbRating2;
			else diff = imdbRating2-imdbRating1;
			sumOfMatchingAttributes += (1-(diff/10d))*0.6d;
			
			
			diff=0;
			
			if(imdbVote1>imdbVote2)
				diff = imdbVote1-imdbVote2;
			else  diff = imdbVote2-imdbVote1;
			sumOfMatchingAttributes += (1-(diff/1000000d))*0.6d;
			
			
			diff=0;
				
			if(amazonAvgScore1>amazonAvgScore2)
				diff = amazonAvgScore1-amazonAvgScore2;
			else  diff = amazonAvgScore2-amazonAvgScore1;
			sumOfMatchingAttributes += (1-(diff/5d))*0.6d;

			
			diff=0;
			
			if(oscarNominated1>oscarNominated2)
				diff = oscarNominated1-oscarNominated2;
			else  diff = oscarNominated2-oscarNominated1;
			sumOfMatchingAttributes += (1-(diff/11d))*0.6d;
			
			diff=0;
			
			if(oscarWinner1>oscarWinner2)
				diff = oscarWinner1-oscarWinner2;
			else  diff = oscarWinner2-oscarWinner1;
			sumOfMatchingAttributes += (1-(diff/11d))*0.6d;
			
			
			diff=0;
			
			if(otherAwards1>otherAwards2)
				diff = otherAwards1-otherAwards2;
			else  diff = otherAwards2-otherAwards1;
			sumOfMatchingAttributes += (1-(diff/480d))*0.4d;
			
			String keyEmit = m1+","+m2+":";
			
			double similarityScore = (sumOfMatchingAttributes/12d);
			
				similarityScore=Math.round(similarityScore*100.0)/100.0;


			 
			
			if(similarityScore>0.0d){
			context.write( new Text(keyEmit), new Text(String.valueOf(similarityScore)+",y"));
			}
			
		}catch(ArrayIndexOutOfBoundsException e){
			
			System.out.println("The values for it is gone "+ value);
			e.printStackTrace();
		}}}
		catch(ArrayIndexOutOfBoundsException e){
			
			System.out.println("The keys for it is crazy "+ key.toString());
			e.printStackTrace();
		}
		
		
	
	}}