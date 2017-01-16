package recommendMoviesJ7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
//emit the input as is
public class Job7Mapper extends Mapper<Object, Text, Text, Text> {
	
	
	private Set<String> moviesWatched = new HashSet<String>();
	private String userid="";
	
	
	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		
		//System.out.println("data is "+value);
		String line = value.toString().trim();
		String movie = line.trim().split("#")[0].trim();
		for(String s:moviesWatched)
		{
			if(movie.equalsIgnoreCase(s)){
				String val =line.trim().split("#")[1].trim();
				//System.out.println("user is "+userid+" and "+val);
				context.write(new Text(userid),new Text(val));
				break;
			}
		}
		
		
		
		
		
		
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
		
			moviesWatched.add(movieId);
		}}
		
		br.close();
	}

}