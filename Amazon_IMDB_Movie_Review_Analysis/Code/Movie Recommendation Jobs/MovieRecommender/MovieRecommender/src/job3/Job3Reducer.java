package job3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * Input (UserId,list of (MovieId,Rating;numRatings;sumRatings))
 * cross product the list with itself
 * emit (MivieId_1, MovieId_2, Rating_1;numRatings_1;sumRatings_1, Rating_2;numRatings_2;sumRatings_2)
 * 
 */

public class Job3Reducer extends Reducer<Text, Text, Text, Text> {
	private Text K2 = new Text();
	private Text V2 = new Text();

	static final Comparator<String> MOVIEID_ORDER = new Comparator<String>() {
		public int compare(String s1, String s2) {
			return s1.compareTo(s2);
		}
	};

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		List<String> moviesList = new ArrayList<String>();

		for (Text value : values) {
			moviesList.add(value.toString().trim());
			//System.out.println(value.toString().trim());
		}

		 
		/* sorting is necessary here to avoid duplicate */
		Collections.sort(moviesList, MOVIEID_ORDER);

		/* cross product */
		for (int i = 0; i < moviesList.size(); i++) {
			for (int j = i + 1; j < moviesList.size(); j++) {
				String[] m_arr1 = moviesList.get(i).split(",", 2);
				String[] m_arr2 = moviesList.get(j).split(",", 2);
				
				
				K2.set(m_arr1[0] + ":" +m_arr1[1]+";");
				V2.set(  m_arr2[0]+ ":" + m_arr2[1]);
				context.write(K2, V2);
			}
		}
	}
}