package job2;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* 
 * receives ((MivieId_1, MovieId_2), list of (Ratings_1, Ratings_2))
 * emits ((MovieId_1, MovieId_2), similarity)
 * sample input: ((8,235), (5.0;6;25,3.0;3;8)) 
 * sample output: ((8,235), similarity) 
 * */

public class Job2Reducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		double NdotProd = 0;
		double Nrating1squaredSum = 0;
		double Nrating2squaredSum = 0;
		double mean1 = -1;
		double mean2 = -1;
		double similarity = 0;
		int n = 0;

		for (Text value : values) {
			n++;

			String[] tokens = value.toString().split(",");
			String[] ratings1 = tokens[0].split(";"); /*
													 * ratings =
													 * rating,numRatings
													 * ,sumRatings
													 */
			String[] ratings2 = tokens[1].split(";");
			double rating1 = Double.parseDouble(ratings1[0].trim());
			double rating2 = Double.parseDouble(ratings2[0].trim());

			if (mean1 == -1 || mean2 == -1) {
				double num1 = Double.parseDouble(ratings1[1].trim());
				double sum1 = Double.parseDouble(ratings1[2].trim());
				double num2 = Double.parseDouble(ratings2[1].trim());
				double sum2 = Double.parseDouble(ratings2[2].trim());
				mean1 = sum1 / num1;
				mean2 = sum2 / num2;
			}

			NdotProd += (rating1 - mean1) * (rating2 - mean2);

			Nrating1squaredSum += Math.pow(rating1 - mean1, 2);
			Nrating2squaredSum += Math.pow(rating2 - mean2, 2);
		}

		if (Nrating1squaredSum == 0 || Nrating2squaredSum == 0 || n == 1) {
			
			similarity=0;
			
		}

		similarity = NdotProd
				/ (Math.sqrt(Nrating1squaredSum) * Math
						.sqrt(Nrating2squaredSum));
		similarity=Math.round(similarity*100.0)/100.0;
		if(similarity>0.0d){
		context.write( key, new Text(": "+String.valueOf(similarity)+",x"));}
	}
}