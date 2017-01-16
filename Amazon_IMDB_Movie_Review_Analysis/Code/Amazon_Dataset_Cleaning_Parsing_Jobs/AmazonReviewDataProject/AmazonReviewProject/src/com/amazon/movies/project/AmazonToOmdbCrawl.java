package com.amazon.movies.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AmazonToOmdbCrawl {

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			/*
			 * Here the input is a line containing ID Title Score we are
			 * canonicalizing the title field and sending it as key to reducer
			 */
			String[] id_title = value.toString().split("\t");
			String line = null;
			try {
				 line = id_title[1].trim();
				/*line = line.replaceAll("\\<.*?>", "");*/
				
				line = line.replaceAll("\\[.*?\\] ?", "").trim();
				 line = line.replaceAll("\\(.*?\\) ?", "").trim();
				line = line.replaceAll("^[\\//\\-\\+\\.\\,\\*]*", "").trim();
				// line = line.replaceAll("^\\s+", "");
				if (!line.isEmpty())
					context.write(new Text(line), value);
			} catch (Exception e) {

			}

		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, NullWritable> {

		/*
		 * Here the title recieved is sent to OMDB, which results a JSON
		 * response the response is finally stored in a file, thus we get part-r
		 * files of omdb responses.
		 */

		@SuppressWarnings("unchecked")
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			String title = key.toString().trim();
			String amazonTitle = title.replace("\"", "").trim();
			//title = title.replaceAll("^[\\-\\+\\.\\,]", " ").trim();
			//title = title.replaceAll("[VHS]", " ").trim();
			//title = title.replace("\"", "");
			title = title.replaceAll("%", "%25").trim();
			title = title.replaceAll("/  +/g"," ").trim();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
			String valueStr[] = null;
			for (Text value : values) {
				valueStr = value.toString().split("\t");
				break;
			}
			for (String l : title.split("/", 2)) {
				//System.out.println("request............."+l);
			
				String r = getResponse(l.trim());
				//System.out.println("response............"+r);
				try {
					if (r != null)
						jsonObject = (JSONObject) jsonParser
								.parse(new String(r));
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (jsonObject != null
						&& !jsonObject.get("Response").equals("False")) {
					jsonObject.put("AmazonTitle", amazonTitle);
					if (valueStr != null && valueStr.length > 1) {
						jsonObject.put("AmazonID", valueStr[0]);
						jsonObject.put("AmazonAvgScore", valueStr[2]);
					} else {
						jsonObject.put("AmazonID", "");
						jsonObject.put("AmazonAvgScore", "");
					}
					// System.out.println("json......"+jsonObject);
					context.write(new Text(jsonObject.toJSONString()),
							NullWritable.get());
				}
			}
		}

		/*
		 * This function takes care of handling the http connection and
		 * resturning Json response from the server.
		 */
		public static String getResponse(String name)
				throws InterruptedException, IOException {
			boolean notSuccess = true;
			URL url = null;
			BufferedReader rd = null;
			while (notSuccess) {
				try {
					url = new URL("http://www.omdbapi.com/?t="+ name.replaceAll(" ", "+")+ "&y=&plot=short&r=json");
					rd = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
					notSuccess = false;
				} catch (Exception e) {
					System.out.println(e);
					Thread.sleep(1000);
				}
			}

			String result = rd.readLine();
			rd.close();
			return result;
		}
	}

	public static void main(String args[]) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();

		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "Movie Filter");
		job.setJarByClass(AmazonToOmdbCrawl.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setNumReduceTasks(10);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		// job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job,new Path(
								"C:\\Users\\inigam\\Documents\\Semester\\Amazon-Movie-Review-Analysis-master\\output_review3New1\\inputDisk3.txt"));
		FileOutputFormat.setOutputPath(job, new Path(args[0]));

		job.waitForCompletion(true);
	}

}
