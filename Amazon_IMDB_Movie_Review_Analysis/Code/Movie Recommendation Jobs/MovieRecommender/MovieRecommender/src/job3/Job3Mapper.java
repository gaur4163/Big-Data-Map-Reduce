package job3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
//emit the input as is
public class Job3Mapper extends Mapper<Object, Text, Text, Text> {
	
	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();
		context.write(new Text("1"),value);
	}

}