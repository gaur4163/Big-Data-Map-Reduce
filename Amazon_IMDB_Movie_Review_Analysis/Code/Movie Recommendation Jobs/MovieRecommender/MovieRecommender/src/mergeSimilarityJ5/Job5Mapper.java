package mergeSimilarityJ5;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
//emit the input as is
public class Job5Mapper extends Mapper<Object, Text, Text, Text> {
	
	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();
		String [] moviedata = line.split(":");
		
		String [] movies = moviedata[0].split(",");
		String k = movies[0].trim()+","+movies[1].trim();
		
		context.write(new Text(k),new Text(moviedata[1].trim()));
		
	}

}