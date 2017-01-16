package job4;

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

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
 *  Input (MoviedId,Director,Actor,ImdbRating,imdbVote,AmazonAvgScore,Year,Language,Country,Genre,OscarWinner,OscarNominated,otherAwards)
 * 	This input file is a csv file which has all the details from imdb server, the fields mentioned above will be fetched by our program
 *  Output (MivieId_1, MovieId_2, Actors: Movie1_actors,Movie2_actors similarly other attributes last terminated by ;)
 */

public class Job4Driver extends Configured implements Tool {

	private static Logger THE_LOGGER = Logger.getLogger(Job4Driver.class);

	@Override
	public int run(String[] args) throws Exception {
		Job job = new Job(getConf());
		job.setJarByClass(Job4Driver.class);
		job.setJobName("Job1");

		//job.setInputFormatClass(FileInputFormat.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		/* compress the final output */
		/*
		FileOutputFormat.setCompressOutput(job, true);
		FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
		*/
		

		job.setMapperClass(Job4Mapper.class);
		job.setReducerClass(Job4Reducer.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean status = job.waitForCompletion(true);
		THE_LOGGER.info("run(): status=" + status);
		return status ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		// Make sure there are exactly 2 parameters
		if (args.length < 2) {
			THE_LOGGER.warn("usage Job1Driver <input> <output>");
			System.exit(1);
		}

		THE_LOGGER.info("inputDir=" + args[0]);
		THE_LOGGER.info("outputDir=" + args[1]);
		int returnStatus = ToolRunner.run(new Job4Driver(), args);
		System.exit(returnStatus);
	}

}
