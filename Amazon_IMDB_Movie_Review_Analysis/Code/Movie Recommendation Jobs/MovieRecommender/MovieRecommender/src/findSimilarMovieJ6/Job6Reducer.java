package findSimilarMovieJ6;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* 
 * receives ((MivieId_1, MovieId_2), list of (Ratings_1, Ratings_2))
 * emits ((MovieId_1, MovieId_2), similarity)
 * sample input: ((8,235), (5.0;6;25,3.0;3;8)) 
 * sample output: ((8,235), similarity) 
 * */

public class Job6Reducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		//HashMap<String, Double> list = new HashMap<String, Double>();
		String similarMovies = new String("");
		String k = null;
		Double v =null;
		HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		for (Text value : values) {
			String [] data = value.toString().split(":");
			 k = data[0];
			 v = Double.parseDouble(data[1]);
			 map.put(k, v);
			//similarMovies= similarMovies.concat(value+",");
		}
		
		sorted_map.putAll(map);
		int count=10;
		for (Entry<String, Double> entry : sorted_map.entrySet()) {
			if(count>0)
			{	String setval = entry.getKey().toString().trim()+":"+String.valueOf(entry.getValue());
				similarMovies= similarMovies.concat(setval+",");	
			}
			count--;
		}
		
		context.write( key, new Text("# "+similarMovies));
		
			
		
	}
	
	class ValueComparator implements Comparator<String> {
	    Map<String, Double> base;

	    public ValueComparator(Map<String, Double> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with
	    // equals.
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}

}