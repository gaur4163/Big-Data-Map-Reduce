package recommendMoviesJ7;

import org.apache.log4j.Logger;

import job3.Job3Mapper;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
 *
 */

public class Job7Driver extends Configured implements Tool {

	private static Logger THE_LOGGER = Logger.getLogger(Job7Driver.class);

	@Override
	public int run(String[] args) throws Exception {
			
		Job job = new Job(getConf());
		job.setJarByClass(Job7Driver.class);
		job.setJobName("Job7");
		job.getConfiguration().set("usersFile", args[1]);
		job.getConfiguration().set("userid", args[0]);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		
		

		job.setMapperClass(Job7Mapper.class);
		job.setReducerClass(Job7Reducer.class);

		
		FileInputFormat.addInputPath(job, new Path(args[2]));
		FileOutputFormat.setOutputPath(job, new Path(args[3]));

		boolean status = job.waitForCompletion(true);
		THE_LOGGER.info("run(): status=" + status);
		return status ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		// Make sure there are exactly 2 parameters
		if (args.length < 3) {
			THE_LOGGER.warn("usage Job7Driver <user Id> <user rating file> <movie similarity file> <output>");
			System.exit(1);
		}

		THE_LOGGER.info("inputDir=" + args[1]);
		THE_LOGGER.info("outputDir=" + args[3]);
		int returnStatus = ToolRunner.run(new Job7Driver(), args);
		System.exit(returnStatus);
	}

}
