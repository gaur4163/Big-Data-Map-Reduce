package job1;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Job1Mapper extends Mapper<Object, Text, Text, Text> {
	
	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();
		String[] contents = line.split(":");
		Text k = new Text(contents[0].trim());
		Text V = new Text(contents[1].trim());
		
		context.write(k,V);
	}

}