package com.amazon.movies.project;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import org.apache.hadoop.mapreduce.Mapper.Context;
public class AmazonTitleAPICrawl {
		/*
		 * Here we are extracting the productid field and review score field and sending it as 
		 * key value pair to reducer.
		 * 
		 */

		public static class MapperJob extends Mapper<LongWritable, Text, Text, DoubleWritable> {

			private static final String MATCHING_PRODUCT="product/productId";
			private static final String MATCHING_SCORE="review/score";
			private static Text word = new Text();
			private static DoubleWritable one = new DoubleWritable();
			
			protected void map(LongWritable fileOffset, Text lineContents, Context context) throws IOException, InterruptedException {	
				String line = lineContents.toString();
				if(line.toLowerCase().contains(MATCHING_PRODUCT.toLowerCase())){
					String[] contentsOfString = line.split(":");
					word.set(contentsOfString[1].trim());
				}
				if(line.toLowerCase().contains(MATCHING_SCORE.toLowerCase())){
					String[] contents = line.split(":");
					one.set(Double.parseDouble(contents[1].trim()));
				}
				context.write(word, one);
				}
			}


		public static class ReviewReducer extends Reducer<Text, DoubleWritable, Text, Text> {
			
			static int count=0;
			
			protected void reduce(Text MovieId, Iterable<DoubleWritable> scores,
					Context context) throws IOException, InterruptedException {
				double totalScore = 0;
				int countForAverage =0;
				for (DoubleWritable score : scores) {
					countForAverage++;
					totalScore += score.get();
					
				}
				
				ProductSearch p= new ProductSearch();
				double averageScore=totalScore/countForAverage;
				String title = null;
				//System.out.println("count.................."+count);
				if(count<1){
				try {
					title = p.getMovieDetails(MovieId.toString());
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(title!=null){
					String outputString = (String) title +"\t"+ averageScore;
					context.write(MovieId,new Text(outputString));
				
				}
				count++;
				}else{
					count=0;
					Thread.sleep(1000);
				}
				
			}
		}
	


		public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException{
			Configuration conf = new Configuration();
			GenericOptionsParser parser = new GenericOptionsParser(conf, args);
			args = parser.getRemainingArgs();
			
			Job job = new Job(conf, "Title Crawling");
			job.setJarByClass(AmazonTitleAPICrawl.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(DoubleWritable.class);
			job.setNumReduceTasks(10);

			job.setMapperClass(MapperJob.class);
			job.setReducerClass(ReviewReducer.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));

			job.waitForCompletion(true);
		}
	
	}
