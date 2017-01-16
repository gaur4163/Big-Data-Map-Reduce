package mergeSimilarityJ5;

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

public class Job5Reducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		try{
			
		String [] movies = key.toString().split(",");
		String m1 = movies[0];
		String m2 = movies[1];
		String present = null;
		
		double count =0;
		double sum=0;
		double finalscore=0;
		for (Text value : values) {
			try{
				count++;	
				String [] sc= value.toString().split(",");
				//present +=sc[1]+",";
			   double score = Double.parseDouble(sc[0].toString().trim());
			sum+=score;
			}catch(ArrayIndexOutOfBoundsException e){
				
				System.out.println("The values for it is gone "+ value);
				e.printStackTrace();
			}}
		
//			if(!present.matches(".*\\by\\b.*")){
//				return;
//			}
			
		
		finalscore=Math.round((sum/count)*100.0)/100.0;
				
			context.write( key, new Text(": "+String.valueOf(finalscore)));
			
	
	}catch(Exception e){
		
		
		e.printStackTrace();
	}
	}}