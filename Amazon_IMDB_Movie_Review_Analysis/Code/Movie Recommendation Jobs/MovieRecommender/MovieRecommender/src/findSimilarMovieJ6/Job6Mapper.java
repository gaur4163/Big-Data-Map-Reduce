package findSimilarMovieJ6;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
//emit the input as is
public class Job6Mapper extends Mapper<Object, Text, Text, Text> {
	
	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString().trim();
		String [] moviedata = line.split(",");
		
		context.write(new Text(moviedata[0].trim()),new Text(moviedata[1].trim()));
		
		String[] set2 = moviedata[1].split(":");
		
		String newVal = moviedata[0].trim()+":"+set2[1].trim();
		
		context.write(new Text(set2[0].trim()),new Text(newVal));
		
		
	}

}