package recommendMoviesJ7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;

/* 
 * receives ((MivieId_1, MovieId_2), list of (Ratings_1, Ratings_2))
 * emits ((MovieId_1, MovieId_2), similarity)
 * sample input: ((8,235), (5.0;6;25,3.0;3;8)) 
 * sample output: ((8,235), similarity) 
 * */

public class Job7Reducer extends Reducer<Text, Text, Text, Text> {

	private Map<String, Double> moviesWatched = new HashMap<String, Double>();
	private String userid="";
	
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		
		String similarMovies = new String("");
		String k = null;
		Double v =null;
		HashMap<String, Double> map = new HashMap<String, Double>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		for (Text value : values) {
			String [] data = value.toString().split(",");
			for(String m:data){
				String [] s = m.toString().split(":");
				
				for(Entry<String, Double> entry : moviesWatched.entrySet()){
					String watched = entry.getKey();
					double rating = entry.getValue();
					if(!watched.equalsIgnoreCase(s[0])){
						k = s[0];
						 v =Math.round((Double.parseDouble(s[1])*rating)*100.0)/100.0 ;
						 System.out.println("The movie is "+k+" weighted rate "+v);
						 
						 if(map.containsKey(k)){
							 System.out.println("The movie already present "+k);
							 double val = map.get(k);
							 if(v>val){
								 System.out.println("changing value for "+k+" to value "+v);
								 map.put(k, v);}
						 }
						 else
						 map.put(k, v);
					}
				}
				
			}
			
			
			
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
	
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		Configuration conf = context.getConfiguration();
		String usersFile = conf.get("usersFile");
		userid = conf.get("userid");
		
		File usermoviefile = new File(usersFile);
		
		BufferedReader br = new BufferedReader(new FileReader(usermoviefile));
		String line = null;
		while ((line = br.readLine()) != null) {
			//System.out.println(line);
			String [] contents = line.split(":");
			String user = contents[0].trim();
			if(user.equalsIgnoreCase(userid)){
			String movieId = ( contents[1].trim().split(",")[0]).trim();
			double movieRating =Double.parseDouble(( (contents[1].trim().split(",")[1]).split(";")[0]).trim());
			moviesWatched.put(movieId,movieRating);
		}}
		
		br.close();
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