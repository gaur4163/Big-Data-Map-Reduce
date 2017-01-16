package com.amazon.movies.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AmazonInputRecordFetcher {

	public static void main(String arg[]) {
		// TODO Auto-generated constructor stub
		try {
			FileReader fileReader = new FileReader("C://Users//inigam//Documents//Semester//Amazon-Movie-Review-Analysis-master//input//movies.txt");
			BufferedReader buf =new BufferedReader(fileReader);
			String line;
			int count=0;
			while(( line=buf.readLine())!=null){
				count++;
				if(line.contains("B0001XLXGQ")){
					System.out.println("got it.........."+line+" "+count);
					break;
				}
		
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("completed");
	}

}
