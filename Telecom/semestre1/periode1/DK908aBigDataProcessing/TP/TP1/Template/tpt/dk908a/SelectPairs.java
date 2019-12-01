package tpt.dk908a;

import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.log4j.Logger;

//1) how do i receive as input the pair count map returned by the count paris??

public class SelectPairs {
    
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

	    public void map(LongWritable offset, javax.xml.soap.Text moviePair, Context context)
	         throws IOException, InterruptedException {
	    	
	    	/*The input looks like (Mid1,Mid2)->(#occurences)*/
	    	
		        String input[] = moviePair.toString().split("\t");
		        
		        int occurences = Integer.parseInt(input[1]);
		        String[] movieIds = input[0].split(",");
	       /*The mapping will produce an output like: Mid1->Mid2,#occurences. So for each movie as key, the number of times it coappears with another one s value with the Mid*/
		        
		        //write the first pair: Mid1->(Mid2,#occurences)
		        Text key = new Text(movieIds[0]);
		        Text value = new Text(movieId[1]+","+occurences);
		        context.write(key,value);
		        //write the first pair: Mid1->(Mid2,#occurences)
		        Text key = new Text(movieIds[1]);
		        Text value = new Text(movieId[0]+","+occurences);
		        context.write(key,value);
		        
	     }
	
	    
    }
    
    /*after the shuffle, i have something like mid1->mid2,3 mid4,7 ect .. so i just have to find the one with the highest value*/
    public static class Reduce extends Reducer<Text, Text, Text, Text> {	
	@Override
	   public void reduce(Text movie, Iterable<Text> values, Context context) 
	      throws IOException, InterruptedException {
	       
		   String movieId=null;
		   int max = 0;
		   
		   //iterate over the set to find the movie with the higest occurence number
		   for(javax.xml.soap.Text value : values) {
			   String[] elements = value.toString().split(",");
			   int value = Integer.parseInt(elements[1]); 
			    if(value > max) {
			    	max =value;
			    	movieId = elements[0];
			    }
		   }
		
	    context.write(movie,new Text(movieId+","max));
	   }
    }
}


