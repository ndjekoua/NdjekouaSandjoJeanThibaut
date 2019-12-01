package tpt.dk908a;

import java.io.IOException;
import java.util.regex.Matcher;
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


public class ExtractUserMovies {
    
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {

	    public void map(LongWritable offset, Text moviePair, Context context)
	      throws IOException, InterruptedException {
		
	       private static final Pattern p = Pattern.compile("^([0-9]+),([0-9]+),([0-9]+),[0-9]+"); //pattern for parsing each input line of Rating(UId,MId,Rating,time)
		      Matcher m = p.matcher(moviePair.toString());
		    
		      //the header will not be parsed because it does not match the pattern
               if (m.find()) {
                  String movieId = m.group(2);
                  Text userId = new Text(m.group(1));
                 
                   Float rating = Float.parseFloat(m.group(3));
                     if(rating > 3.5) {
                	  
                     	//set the key as Text in the field word 
                	    Text value = new Text(movieId);
                	    //create the mapping key value pair and send it on the output
                         context.write(userId,value);
                     }
               }
	    }
    }
     
    /*after the shuffle is completed i will have pairs like (UserId,list containing movies)*/
    public static class Reduce extends Reducer<Text, Text, Text, Text> {	
	@Override
	  public void reduce(Text userId, Iterable<Text> values, Context context) 
	     /*now i have to concatenate the list so that each element can be distinguisahble when passing it as input to the next job*/
	    String StringValues;
	    int size = values.length();
	    for (value : values) {
	    	
	    	//the control is used to avoid adding a "," after the last element of the list
	    	size--;
	    	if(size != 0) {
	    		StringValues+=value+",";
	    	}else {StringValues+=value;}
	    	
	    }
	      context.write(userId,new Text(Stringvalues));/*the output should be like: UserId -> MovieId1,MovieId2ect...*/
	  }
    }
}


